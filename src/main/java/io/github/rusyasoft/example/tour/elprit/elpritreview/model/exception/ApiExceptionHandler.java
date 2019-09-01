package io.github.rusyasoft.example.tour.elprit.elpritreview.model.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({ ElpirtException.class })
    protected ResponseEntity<ApiErrorResponse> handleApiException(ElpirtException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(ex.getHttpStatus(), ex.getMessage(), Instant.now()), ex.getHttpStatus());
    }
}