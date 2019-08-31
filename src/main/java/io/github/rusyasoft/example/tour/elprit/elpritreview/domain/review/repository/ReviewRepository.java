package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.repository;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.UniquePlaceUserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, UniquePlaceUserReview> {

//    boolean existsByPlaceId(String placeId);

    @Query(value = "select count(*)>0 from Review r where r.place_id = ?1", nativeQuery = true)
    public boolean reviewExistForPlace(String placeId);
}
