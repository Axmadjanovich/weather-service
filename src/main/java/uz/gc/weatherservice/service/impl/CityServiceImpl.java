package uz.gc.weatherservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.CityDto;
import uz.gc.weatherservice.dto.ResponseDto;
import uz.gc.weatherservice.repository.CityRepository;
import uz.gc.weatherservice.service.CityService;
import uz.gc.weatherservice.service.mapper.CityMapper;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public Mono<ResponseDto<CityDto>> addCity(CityDto dto) {
        return cityRepository.save(cityMapper.toEntity(dto))
                .map(c -> ResponseDto.<CityDto>builder()
                        .data(cityMapper.toDto(c))
                        .success(true)
                        .message("OK")
                        .build());
    }

    @Override
    public Flux<CityDto> getCitiesList() {
        return cityRepository.findAllByVisible(true)
                .map(cityMapper::toDto);
    }

    @Override
    public Mono<ResponseDto<CityDto>> editCity(CityDto dto) {
        return cityMapper.toEditEntity(dto)
                .flatMap(cityRepository::save)
                .map(c -> ResponseDto.<CityDto>builder()
                            .data(cityMapper.toDto(c))
                            .success(true)
                            .message("OK")
                            .build()
                ).defaultIfEmpty(ResponseDto.<CityDto>builder()
                        .message("City with ID " + dto.getId() + " is not found")
                        .code(-1)
                        .build());
    }

    @Override
    public Mono<CityDto> getById(Integer id) {
        return cityRepository.findById(id)
                .map(cityMapper::toDto);
    }
}
