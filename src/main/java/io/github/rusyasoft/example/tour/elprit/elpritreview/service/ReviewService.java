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

        User resultUser = null;
        switch (eventParam.getAction()) {
            case ADD: resultUser = placeService.addReview(review); break;
            case MOD: resultUser = placeService.modifyReview(review); break;
            case DELETE: resultUser = placeService.deleteReview(review); break;
        }

        return resultUser;
    }

}
