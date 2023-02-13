package uz.gc.weatherservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.WeatherDto;
import uz.gc.weatherservice.model.Users;
import uz.gc.weatherservice.repository.CityRepository;
import uz.gc.weatherservice.repository.WeatherRepository;
import uz.gc.weatherservice.service.WeatherService;
import uz.gc.weatherservice.service.mapper.CityMapper;
import uz.gc.weatherservice.service.mapper.WeatherMapper;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final WeatherMapper weatherMapper;
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public Mono<WeatherDto> setWeather(WeatherDto weather) {
        return weatherRepository.save(weatherMapper.toEntity(weather))
                .map(weatherMapper::toDto)
                .flatMap(this::loadCity);
    }

    @Override
    public Flux<WeatherDto> getSubscriptionsWeather() {
        return ReactiveSecurityContextHolder.getContext()
                .flux()
                .flatMap(auth -> {
                    Users u = (Users) auth.getAuthentication().getPrincipal();
                    return weatherRepository.getAllWeatherForUser(u.getId())
                            .map(weatherMapper::toDto)
                            .flatMap(this::loadCity);
                });
    }

    private Mono<WeatherDto> loadCity(WeatherDto dto){
        return Mono.just(dto)
                .zipWith(cityRepository.findById(dto.getCity().getId()))
                .map(result -> {
                    result.getT1().setCity(cityMapper.toDto(result.getT2()));
                    return result.getT1();
                });
    }
}
