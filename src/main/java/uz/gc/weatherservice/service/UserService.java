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

    /**
     * Change user info as PATCH method. Updates only given fields.
     * @param usersDto Requested object to update
     * @return ResponseDto of updated User
     */
    Mono<ResponseDto<UsersDto>> editUser(UsersDto usersDto);

    /**
     * Get user by userId
     * @param id ID of user
     * @return Reactive response of UsersDto
     */
    Mono<UsersDto> getUserById(Integer id);

    /**
     * Getting token with email and password.
     * @param loginDto email and password
     * @return ResponseDto with JWT token string
     */
    Mono<ResponseDto<String>> getToken(LoginDto loginDto);

    /**
     * Giving authority to user. This authority is used for protect or access some methods
     * @param userId User ID that is giving authority
     * @param roleId Giving authority to user
     * @return Authority ID
     */
    Mono<String> addRole(Integer userId, Integer roleId);
}
