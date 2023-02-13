package uz.gc.weatherservice.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.gc.weatherservice.dto.CityDto;
import uz.gc.weatherservice.dto.SubscriptionDto;
import uz.gc.weatherservice.model.Subscriptions;

@Mapper(componentModel = "spring", imports = CityDto.class)
public interface SubscriptionMapper extends CommonMapper<SubscriptionDto, Subscriptions> {

    @Mapping(target = "city", expression = "java(CityDto.builder().id(entity.getCityId()).build())")
    @Mapping(target = "subscribedAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    SubscriptionDto toDto(Subscriptions entity);
}
