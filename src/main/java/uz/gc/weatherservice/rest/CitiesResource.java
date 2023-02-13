package uz.gc.weatherservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.CityDto;
import uz.gc.weatherservice.dto.ResponseDto;
import uz.gc.weatherservice.service.CityService;

@RestController
@RequestMapping("cities")
@RequiredArgsConstructor
public class CitiesResource {

    private final CityService cityService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public Mono<ResponseDto<CityDto>> addCity(@RequestBody CityDto dto){
        return cityService.addCity(dto);
    }

    @GetMapping("cities-list")
    public Flux<CityDto> getCitiesList(){
        return cityService.getCitiesList();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("edit-city")
    public Mono<ResponseDto<CityDto>> editCity(@RequestBody CityDto dto){
        return cityService.editCity(dto);
    }
}
