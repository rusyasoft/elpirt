package io.github.rusyasoft.example.tour.elprit.elpritreview.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ElpirtException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ElpirtException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
