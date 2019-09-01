package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.place.exception;

import io.github.rusyasoft.example.tour.elprit.elpritreview.model.exception.ElpirtException;
import org.springframework.http.HttpStatus;

public class PlaceNotFoundException extends ElpirtException {

    public PlaceNotFoundException(String errorMessage) {
        super(HttpStatus.NOT_FOUND, errorMessage);
    }
}