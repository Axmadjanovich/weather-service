package uz.gc.weatherservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseDto<T> {
    /**
     * Response code that represents status of response.
     * -3 - UNEXPECTED ERROR
     * -2 - DATABASE ERROR
     * -1 - NOT FOUND
     * 0 - OK
     * 2 - VALIDATION ERROR
     */
    private int code;
    /**
     * Response status represents that request is failed or not
     */
    private boolean success;
    /**
     * Response message. Describes errors or successful response messages
     */
    private String message;
    /**
     * Generic data of Response
     */
    private T data;
}
