package uz.gc.weatherservice.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.CityDto;
import uz.gc.weatherservice.dto.ResponseDto;


public interface CityService {

    /**
     * Adding new City
     * @param dto requested object to add
     * @return ResponseDto with added City information
     */
    Mono<ResponseDto<CityDto>> addCity(CityDto dto);

    /**
     * Getting all visible cities.
     * @return Flux of fetched cities.
     */
    Flux<CityDto> getCitiesList();

    /**
     * Update city info. Works as patching method. Updates only changed fields.
     * @param dto Requested City for updating
     * @return ResponseDto with updated City
     */
    Mono<ResponseDto<CityDto>> editCity(CityDto dto);

    /**
     * Getting City with its ID
     * @param id ID of city
     * @return Mono of fetched City
     */
    Mono<CityDto> getById(Integer id);
}
