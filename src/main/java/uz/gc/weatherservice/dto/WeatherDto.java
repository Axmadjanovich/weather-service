package uz.gc.weatherservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WeatherDto {
    private Integer id;
    private CityDto city;
    private Integer temperature;
    private Integer wind;
    private Integer humidity;
    private Integer visibility;
    private Integer pressure;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDateTime date;
}
