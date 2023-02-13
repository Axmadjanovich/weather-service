package uz.gc.weatherservice.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.UsersDto;
import uz.gc.weatherservice.model.Users;
import uz.gc.weatherservice.repository.UsersRepository;

@Mapper(componentModel = "spring")
public abstract class UsersMapper implements CommonMapper<UsersDto, Users> {

    @Lazy
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    protected PasswordEncoder encoder;

    public Mono<Users> toEditEntity(UsersDto dto){
        return usersRepository.findById(dto.getId())
                .map(u -> {
                    if (dto.getEmail() != null) u.setEmail(dto.getEmail());
                    if (dto.getFirstName() != null) u.setFirstName(dto.getFirstName());
                    if (dto.getLastName() != null) u.setLastName(dto.getLastName());
                    if (dto.getPhoneNumber() != null) u.setPhoneNumber(dto.getPhoneNumber());
                    return u;
                });
    }

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    @Mapping(target = "password", expression = "java(null)")
    public abstract UsersDto toDto(Users users);

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = "dd.MM.yyyy HH:mm:ss")
    @Mapping(target = "password", expression = "java(encoder.encode(dto.getPassword()))")
    public abstract Users toEntity(UsersDto dto);
}
