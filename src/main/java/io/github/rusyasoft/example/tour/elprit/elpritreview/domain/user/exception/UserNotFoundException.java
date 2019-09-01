package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.exception;

import io.github.rusyasoft.example.tour.elprit.elpritreview.model.exception.ElpirtException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ElpirtException {

    public UserNotFoundException(String errorMessage) {
        super(HttpStatus.NOT_FOUND, errorMessage);
    }
}