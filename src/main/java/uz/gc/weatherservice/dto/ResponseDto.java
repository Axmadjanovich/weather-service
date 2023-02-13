package uz.gc.weatherservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseDto<T> {
    private int code;
    private boolean success;
    private String message;
    private T data;
}
