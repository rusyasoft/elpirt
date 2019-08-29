package io.github.rusyasoft.example.tour.elprit.elpritreview.service;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.UniquePlaceUserReview;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.dto.EventParam;
import io.github.rusyasoft.example.tour.elprit.elpritreview.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public void addEvent(EventParam eventParam) {
        // check the user has a review or not
        if (doesUserHasReviewForPlace(eventParam)) {
            throw new RuntimeException("Review already Exist");
        }

        if (isItFirstReviewForPlace(eventParam)) {

        }

    }

    private boolean doesUserHasReviewForPlace(EventParam eventParam) {
        String userId = eventParam.getUserId();
        String placeId = eventParam.getPlaceId();

        UniquePlaceUserReview uniquePlaceUserReview = UniquePlaceUserReview.builder()
                .placeId(placeId)
                .userId(userId)
                .build();

        Optional<Review> optionalReview = reviewRepository.findById(uniquePlaceUserReview);

        return optionalReview.isPresent();

    }

    private boolean isItFirstReviewForPlace(EventParam eventParam) {
        String placeId = eventParam.getPlaceId();

        return reviewRepository.reviewExistForPlace(placeId);

    }

}
