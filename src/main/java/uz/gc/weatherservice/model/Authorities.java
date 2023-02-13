package uz.gc.weatherservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
public class Authorities {
    @Id
    private Integer id;
    private String name;
}
