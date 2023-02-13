package uz.gc.weatherservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.model.Subscriptions;

public interface SubscriptionRepository extends ReactiveCrudRepository<Subscriptions, Integer> {
    @Query("select s.* from subscriptions s " +
            "left join city c on c.id = s.city_id " +
            "where c.visible is true and s.user_id = :userId and c.id = :cityId")
    Mono<Subscriptions> findFirstByUserIdAndCityId(Integer userId, Integer cityId);
    Flux<Subscriptions> findAllByUserId(Integer userId);
}
