package uz.gc.weatherservice.rest;

import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.ResponseDto;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler
    public Mono<ResponseDto<Void>> dataIntegrityViolationException(DataIntegrityViolationException e){
        return Mono.just(ResponseDto.<Void>builder()
                        .code(-2)
                        .message("Error while saving data: " + cause(e))
                .build());
    }

    @ExceptionHandler
    public Mono<ResponseDto<Void>> illegalArgumentException(IllegalArgumentException e){
        return Mono.just(ResponseDto.<Void>builder()
                        .code(-2)
                        .message("Illegal argument exception: " + cause(e))
                .build());
    }

    @ExceptionHandler
    public Mono<ResponseDto<Void>> unexpectedException(Exception e){
        return Mono.just(ResponseDto.<Void>builder()
                .code(-3)
                .message("Unexpected exception occurred: " + cause(e))
                .build());
    }

    @ExceptionHandler
    public Mono<ResponseEntity<String>> tokenInvalidException(MalformedJwtException e){
        return Mono.just(ResponseEntity.badRequest().body("Malformed JWT token"));
    }

    private String cause(Exception e){
        StringBuilder cause = new StringBuilder(e.getMessage());
        Throwable t = e.getCause();

        cause.append(" Caused by: ");
        while (t != null){
            cause.append(t.getMessage());
            t = t.getCause();
        }
        return cause.toString();
    }
}
