package uz.gc.weatherservice.service;

import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.LoginDto;
import uz.gc.weatherservice.dto.ResponseDto;
import uz.gc.weatherservice.dto.UsersDto;

public interface UserService{

    /**
     * Adding new User to database
     * @param dto User's object that sent from client
     * @return Reactive response of UsersDto wrapped with ResponseDto
     */
    Mono<ResponseDto<UsersDto>> addUser(UsersDto dto);

    Mono<ResponseDto<UsersDto>> editUser(UsersDto usersDto);

    Mono<UsersDto> getUserById(Integer id);

    Mono<ResponseDto<String>> getToken(LoginDto loginDto);

    Mono<String> addRole(Integer userId, Integer roleId);
}
