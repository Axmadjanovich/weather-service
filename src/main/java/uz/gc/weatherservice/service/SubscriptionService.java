package uz.gc.weatherservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.ResponseDto;
import uz.gc.weatherservice.dto.SubscriptionDto;

public interface SubscriptionService {
    Mono<ResponseDto<SubscriptionDto>> subscribe(Integer cityId);

    Flux<SubscriptionDto> getAllSubscriptions();

    Flux<SubscriptionDto> getUserSubscriptions(Integer userId);
}
