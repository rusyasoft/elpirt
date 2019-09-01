package io.github.rusyasoft.example.tour.elprit.elpritreview.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class ApiErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final Instant timestamp;

}
