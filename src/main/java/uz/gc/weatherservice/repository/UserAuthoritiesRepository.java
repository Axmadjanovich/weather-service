package uz.gc.weatherservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import uz.gc.weatherservice.model.Roles;

@Repository
public interface UserAuthoritiesRepository extends ReactiveCrudRepository<Roles, Integer> {
    Flux<Roles> findAllByUserId(Integer id);
}
