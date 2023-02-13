package uz.gc.weatherservice.service.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.CityDto;
import uz.gc.weatherservice.model.City;
import uz.gc.weatherservice.repository.CityRepository;
import uz.gc.weatherservice.service.CityService;

@Mapper(componentModel = "spring")
public abstract class CityMapper implements CommonMapper<CityDto, City> {

    @Autowired
    @Lazy
    private CityRepository cityService;

    public Mono<City> toEditEntity(CityDto dto){
        return cityService.findById(dto.getId())
                .map(c -> {
                    if (dto.getCountry() != null) c.setCountry(dto.getCountry());
                    if (dto.getName() != null) c.setName(dto.getName());
                    if (dto.getShortName() != null) c.setShortName(dto.getShortName());
                    if (dto.getVisible() != null) c.setVisible(dto.getVisible());
                    return c;
                });
    }
}
