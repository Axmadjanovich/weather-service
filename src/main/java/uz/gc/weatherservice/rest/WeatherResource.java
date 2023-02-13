package uz.gc.weatherservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.WeatherDto;
import uz.gc.weatherservice.service.WeatherService;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherResource {

    private final WeatherService weatherService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("update-city-weather")
    public Mono<WeatherDto> updateCityWeather(@RequestBody WeatherDto dto){
        return weatherService.setWeather(dto);
    }

    @GetMapping("get-subscriptions")
    public Flux<WeatherDto> getSubscriptionsWeather(){
        return weatherService.getSubscriptionsWeather();
    }
}
