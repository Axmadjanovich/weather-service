package uz.gc.weatherservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.WeatherDto;

public interface WeatherService {

    Mono<WeatherDto> setWeather(WeatherDto weather);

    Flux<WeatherDto> getSubscriptionsWeather();
}
