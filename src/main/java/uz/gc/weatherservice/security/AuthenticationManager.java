package uz.gc.weatherservice.security;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.model.Users;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTService jwtService;
    private final Gson gson;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String userJson = jwtService.username(token);

        if (userJson != null && jwtService.validateToken(token)){
            List<Object> auth = jwtService.getClaim(token, "auth", List.class);
            List<GrantedAuthority> roles = auth.stream()
                    .map(s -> new SimpleGrantedAuthority(s.toString()))
                    .collect(Collectors.toList());

            Users user = gson.fromJson(jwtService.getClaim(token, "sub", String.class), Users.class);
            UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(user, null, roles);
            return Mono.just(authenticatedUser);
        }else {
            return Mono.empty();
        }
    }
}
