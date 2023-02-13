package uz.gc.weatherservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@Setter
public class Weather {
    @Id
    private Integer id;
    private Integer cityId;
    private Integer temperature;
    private Integer wind;
    private Integer humidity;
    private Integer visibility;
    private Integer pressure;
    @CreatedDate
    private LocalDateTime date;
}
