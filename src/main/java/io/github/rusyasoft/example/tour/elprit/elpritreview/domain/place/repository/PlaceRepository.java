package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.place.repository;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.place.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, String> {
}
