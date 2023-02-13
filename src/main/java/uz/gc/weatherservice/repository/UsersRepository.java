package uz.gc.weatherservice.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.model.Users;

@Repository
public interface UsersRepository extends ReactiveCrudRepository<Users, Integer> {
    Mono<Users> findByEmail(String email);
    Mono<Users> deleteByEmail(String email);
}
