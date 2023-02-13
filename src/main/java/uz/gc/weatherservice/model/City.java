package uz.gc.weatherservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City{
    @Id
    private Integer id;
    private String name;
    private String country;
    private String shortName;
    private boolean visible;
}
