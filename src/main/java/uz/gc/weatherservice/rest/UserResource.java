package uz.gc.weatherservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.LoginDto;
import uz.gc.weatherservice.dto.ResponseDto;
import uz.gc.weatherservice.dto.UsersDto;
import uz.gc.weatherservice.service.impl.UsersServiceImpl;

import java.io.Flushable;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserResource {

    private final UsersServiceImpl usersService;

    @PostMapping("sign-up")
    public Mono<ResponseDto<UsersDto>> addUser(@RequestBody UsersDto dto){
        return usersService.addUser(dto);
    }

    @PostMapping("register")
    public Mono<ResponseDto<String>> getToken(@RequestBody LoginDto loginDto){
        return usersService.getToken(loginDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("user-list")
    public Flux<UsersDto> getAllUsers(){
        return usersService.getAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("edit-user")
    public Mono<ResponseDto<UsersDto>> editUser(@RequestBody UsersDto usersDto){
        return usersService.editUser(usersDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("add-role")
    public Mono<String> addRole(@RequestParam Integer userId, @RequestParam Integer roleId){
        return usersService.addRole(userId, roleId);
    }
}
