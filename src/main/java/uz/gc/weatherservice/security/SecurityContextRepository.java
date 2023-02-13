package uz.gc.weatherservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SecurityContextRepository implements ServerSecurityContextRepository {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    /**
     * Gets token from request and validates it. After successfully validation user info will be added to ReactiveSecurityContextHolder
     * @param exchange Request instance
     * @return SecurityContext with authentication info
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.replace("Bearer ","");
        }else {
            log.warn("Authorization header is empty or incorrect");
        }

        if (token != null){
            Authentication authentication = new UsernamePasswordAuthenticationToken(token, token);
            ReactiveSecurityContextHolder.withSecurityContext(Mono.just(new SecurityContextImpl(authentication)));

            return authenticationManager.authenticate(authentication).map(SecurityContextImpl::new);
        } else {
            return Mono.empty();
        }
    }
}
