package uz.gc.weatherservice.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.CityDto;
import uz.gc.weatherservice.dto.ResponseDto;


public interface CityService {

    Mono<ResponseDto<CityDto>> addCity(CityDto dto);

    Flux<CityDto> getCitiesList();

    Mono<ResponseDto<CityDto>> editCity(CityDto dto);

    Mono<CityDto> getById(Integer id);
}
