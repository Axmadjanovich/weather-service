package uz.gc.weatherservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.context.annotation.Profile;
import uz.gc.weatherservice.model.Authorities;
import uz.gc.weatherservice.model.Roles;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(value = "password", allowSetters = true)
@Builder
public class UsersDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private String createdAt;
    private String updatedAt;
    private List<Authorities> roles;
}
