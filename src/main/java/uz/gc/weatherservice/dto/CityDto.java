package uz.gc.weatherservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(value = "isVisible", allowSetters = true)
public class CityDto {
    private Integer id;
    private String name;
    private String country;
    private String shortName;
    private Boolean visible;
}
