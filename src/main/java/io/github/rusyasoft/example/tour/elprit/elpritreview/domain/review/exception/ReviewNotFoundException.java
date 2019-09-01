package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.exception;

import io.github.rusyasoft.example.tour.elprit.elpritreview.model.exception.ElpirtException;
import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends ElpirtException {

    public ReviewNotFoundException(String errorMessage) {
        super(HttpStatus.NOT_FOUND, errorMessage);
    }
}
