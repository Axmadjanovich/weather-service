package uz.gc.weatherservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscriptions {
    @Id
    private Integer id;
    private Integer userId;
    private Integer cityId;
    @CreatedDate
    private LocalDateTime subscribedAt;
}
