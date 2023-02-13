package uz.gc.weatherservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.WeatherDto;

public interface WeatherService {

    /**
     * Update weather of city and users get last changed weather info.
     * @param weather Weather info
     * @return Added weather info
     */
    Mono<WeatherDto> setWeather(WeatherDto weather);

    /**
     * Get all subscribed cities of logged-in user.
     * @return Flux of WeatherDto
     */
    Flux<WeatherDto> getSubscriptionsWeather();
}
