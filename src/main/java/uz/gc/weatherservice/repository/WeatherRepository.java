package uz.gc.weatherservice.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import uz.gc.weatherservice.model.Weather;

@Repository
public interface WeatherRepository extends ReactiveCrudRepository<Weather, Integer> {

    @Query("select w.* from weather w " +
            "where (w.city_id, w.date) in (select w2.city_id, max(w2.date) from weather w2 " +
            "                              where w2.city_id in (select s.city_id from subscriptions s " +
            "                                                   left join city c on c.id = s.city_id " +
            "                                                   where s.user_id = :userId and c.visible is true) " +
            "                              group by w2.city_id)")
    Flux<Weather> getAllWeatherForUser(Integer userId);
}
