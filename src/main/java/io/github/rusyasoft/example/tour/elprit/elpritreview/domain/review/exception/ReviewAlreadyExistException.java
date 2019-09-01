package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.exception;

import io.github.rusyasoft.example.tour.elprit.elpritreview.model.exception.ElpirtException;
import org.springframework.http.HttpStatus;

public class ReviewAlreadyExistException extends ElpirtException {

    public ReviewAlreadyExistException(String errorMessage) {
        super(HttpStatus.CONFLICT, errorMessage);
    }
}
