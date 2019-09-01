package io.github.rusyasoft.example.tour.elprit.elpritreview.service;

import io.github.rusyasoft.example.tour.elprit.elpritreview.model.UniquePlaceUserReview;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.model.User;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.dto.EventParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private PlaceService placeService;

    public User processReviewEvent(EventParam eventParam) {
        switch (eventParam.getAction()) {
            case ADD: return placeService.addReview(eventParamToReview(eventParam));
            case MOD: return placeService.modifyReview(eventParamToReview(eventParam));
            case DELETE: return placeService.deleteReview(eventParamToReview(eventParam));
        }

        return null;
    }

    private Review eventParamToReview(EventParam eventParam) {
        UniquePlaceUserReview uniquePlaceUserReview = UniquePlaceUserReview.builder()
                .placeId(eventParam.getPlaceId())
                .userId(eventParam.getUserId())
                .build();

        Review review = Review.builder()
                .id(eventParam.getReviewId())
                .uniquePlaceUserReview(uniquePlaceUserReview)
                .content(eventParam.getContent())
                .firstReview(false)
                .photoIdList(eventParam.getAttachedPhotoIds())
                .build();

        return review;
    }

}
