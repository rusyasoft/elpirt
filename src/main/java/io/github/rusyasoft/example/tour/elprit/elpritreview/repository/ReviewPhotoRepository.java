package io.github.rusyasoft.example.tour.elprit.elpritreview.repository;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.ReviewPhoto;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.UniquePlaceUserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, String> {

}
