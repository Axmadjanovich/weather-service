package uz.gc.weatherservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.ResponseDto;
import uz.gc.weatherservice.dto.SubscriptionDto;
import uz.gc.weatherservice.model.Subscriptions;
import uz.gc.weatherservice.model.Users;
import uz.gc.weatherservice.repository.CityRepository;
import uz.gc.weatherservice.repository.SubscriptionRepository;
import uz.gc.weatherservice.service.SubscriptionService;
import uz.gc.weatherservice.service.mapper.CityMapper;
import uz.gc.weatherservice.service.mapper.SubscriptionMapper;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public Mono<ResponseDto<SubscriptionDto>> subscribe(Integer cityId) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(u -> {
                    Users user = (Users) u.getAuthentication().getPrincipal();
                    return cityRepository.findFirstByIdAndVisible(cityId, true)
                            .flatMap(city ->
                                    subscriptionRepository.findFirstByUserIdAndCityId(user.getId(), cityId)
                                            .flatMap(s -> Mono.just(subscriptionMapper.toDto(s))
                                                    .flatMap(this::loadCity)
                                                    .map(subscription -> ResponseDto.<SubscriptionDto>builder()
                                                            .data(subscription)
                                                            .message("You already subscribed to this city")
                                                            .code(2)
                                                            .build()))
                                            .switchIfEmpty(Mono.just(
                                                            Subscriptions.builder()
                                                                    .userId(user.getId())
                                                                    .cityId(cityId)
                                                                    .build())
                                                    .flatMap(s ->
                                                            subscriptionRepository.save(s)
                                                                    .map(subscriptionMapper::toDto)
                                                                    .flatMap(this::loadCity)
                                                                    .map(saved -> ResponseDto.<SubscriptionDto>builder()
                                                                            .message("OK")
                                                                            .success(true)
                                                                            .data(saved)
                                                                            .build())
                                                    )
                                            ))
                            .switchIfEmpty(Mono.just(ResponseDto.<SubscriptionDto>builder()
                                    .message("You cannot subscribe to this city, because the city is not visible")
                                    .code(-2)
                                    .build()));
                });
    }

    @Override
    public Flux<SubscriptionDto> getAllSubscriptions() {
       return ReactiveSecurityContextHolder.getContext()
               .flux()
               .flatMap(auth -> {
                   Users u = (Users) auth.getAuthentication().getPrincipal();
                   return subscriptionRepository.findAllByUserId(u.getId())
                           .map(subscriptionMapper::toDto)
                           .flatMap(this::loadCity);
               });
    }

    @Override
    public Flux<SubscriptionDto> getUserSubscriptions(Integer userId) {
        return subscriptionRepository.findAllByUserId(userId)
                .map(subscriptionMapper::toDto)
                .flatMap(this::loadCity);
    }

    private Mono<SubscriptionDto> loadCity(SubscriptionDto dto){
        return Mono.just(dto)
                .zipWith(cityRepository.findById(dto.getCity().getId()))
                .map(result -> {
                    result.getT1().setCity(cityMapper.toDto(result.getT2()));
                    return result.getT1();
                });
    }
}
