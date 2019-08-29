package io.github.rusyasoft.example.tour.elprit.elpritreview.repository;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.UniquePlaceUserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, UniquePlaceUserReview> {

//    boolean existsByPlaceId(String placeId);

    @Query(value = "select count(review)>0 from Review r where r.place_id = ?1", nativeQuery = true)
    public boolean reviewExistForPlace(String placeId);
}
