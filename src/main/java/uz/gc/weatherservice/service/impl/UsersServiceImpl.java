package uz.gc.weatherservice.service.impl;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.LoginDto;
import uz.gc.weatherservice.dto.ResponseDto;
import uz.gc.weatherservice.dto.UsersDto;
import uz.gc.weatherservice.model.Authorities;
import uz.gc.weatherservice.model.Roles;
import uz.gc.weatherservice.model.Users;
import uz.gc.weatherservice.repository.AuthoritiesRepository;
import uz.gc.weatherservice.repository.UserAuthoritiesRepository;
import uz.gc.weatherservice.repository.UsersRepository;
import uz.gc.weatherservice.security.JWTService;
import uz.gc.weatherservice.service.UserService;
import uz.gc.weatherservice.service.mapper.UsersMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UserService, ReactiveUserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final AuthoritiesRepository authoritiesRepository;
    private final UserAuthoritiesRepository userAuthoritiesRepository;
    private final JWTService jwtService;
    private final Gson gson;

    @Profile("test")
    @PostConstruct
    public void updateMockUsersPassword(){
        usersRepository.findAll()
                .collectList()
                .flatMap(u -> {
                    u.forEach(user -> {
                        if (!user.getPassword().startsWith("$2a$10$"))
                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                    });
                    return usersRepository.saveAll(u).collectList();
                });
    }

    public Mono<ResponseDto<UsersDto>> addUser(UsersDto dto) {
        return usersRepository.findByEmail(dto.getEmail())
                .map(dbUser ->
                        ResponseDto.<UsersDto>builder()
                            .code(-2)
                            .message("User with email " + dto.getEmail() + " is already exists")
                            .data(dto)
                            .build())
                .switchIfEmpty(usersRepository.save(usersMapper.toEntity(dto))
//                        .doOnNext(savedUser -> userAuthoritiesRepository.save(new Roles(savedUser.getId(), 1)))
                        .map(savedUser -> ResponseDto.<UsersDto>builder()
                                .success(true)
                                .data(usersMapper.toDto(savedUser))
                                .message("OK")
                                .build())
                );
    }

    @Override
    public Mono<ResponseDto<UsersDto>> editUser(UsersDto usersDto) {
        Mono<Users> users = usersMapper.toEditEntity(usersDto)
                .map(u -> {
                    u.setUpdatedAt(LocalDateTime.now());
                    return u;
                });

        return users.flatMap(usersRepository::save)
                .map(u -> ResponseDto.<UsersDto>builder()
                        .message("OK")
                        .success(true)
                        .data(usersMapper.toDto(u))
                        .build())
                .defaultIfEmpty(ResponseDto.<UsersDto>builder()
                        .message("User with ID " + usersDto.getId() + " is not found")
                        .code(-1)
                        .data(usersDto)
                        .build());
    }

    @Override
    public Mono<UsersDto> getUserById(Integer id) {
        return usersRepository.findById(id).map(usersMapper::toDto);
    }

    @Override
    public Mono<ResponseDto<String>> getToken(LoginDto loginDto) {
        Mono<Users> user = usersRepository.findByEmail(loginDto.getEmail());
        return user.filter(u ->
                        passwordEncoder.matches(loginDto.getPassword(), u.getPassword()))
                .flatMap(this::loadUserWithRoles)
                .map(u -> ResponseDto.<String>builder()
                        .success(true)
                        .message("OK")
                        .data(jwtService.generateToken(gson.toJson(u), u.getRoles().stream().map(Authorities::getName).collect(Collectors.toList())))
                        .build())
                .defaultIfEmpty(ResponseDto.<String>builder()
                        .code(1)
                        .message("Email or password is incorrect")
                        .build());
    }

    private Mono<Users> loadUserWithRoles(Users u){
        return Mono.just(u)
                .zipWith(authoritiesRepository.getAuthoritiesByUserId(u.getId()).collectList())
                .map(result -> {
                    result.getT1().setRoles(result.getT2());
                    return result.getT1();
                });
    }

//    private Mono<Authorities> loadAuthorities(List<Roles> roles){
//        Mono<List<Authorities>>
//    }

    @Override
    public Mono<String> addRole(Integer userId, Integer roleId) {
        Roles role = new Roles();
        role.setUserId(userId);
        role.setAuthorityId(roleId);
        return userAuthoritiesRepository.save(role)
                .map(u -> u.getAuthorityId().toString());
    }

    public Flux<UsersDto> getAll(){
        return usersRepository.findAll()
                .map(usersMapper::toDto);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return usersRepository.findByEmail(username)
                .flatMap(this::loadUserWithRoles);
    }
}
