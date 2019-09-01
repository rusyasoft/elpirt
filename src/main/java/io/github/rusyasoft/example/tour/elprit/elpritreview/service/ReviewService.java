package io.github.rusyasoft.example.tour.elprit.elpritreview.service;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.place.model.Place;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.point.model.PointHistory;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.exception.ReviewAlreadyExistException;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.exception.ReviewNotFoundException;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.ReviewPhoto;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.repository.ReviewPhotoRepository;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.repository.ReviewRepository;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.UniquePlaceUserReview;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.model.User;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.dto.EventParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewPhotoRepository reviewPhotoRepository;

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

    public boolean doesUserHasReviewForPlace(User user, Place place) {
        UniquePlaceUserReview uniquePlaceUserReview = UniquePlaceUserReview.builder()
                .placeId(place.getId())
                .userId(user.getUserId())
                .build();

        return doesUserHasReviewForPlace(uniquePlaceUserReview);
    }

    public boolean doesUserHasReviewForPlace(UniquePlaceUserReview uniquePlaceUserReview) {
        Optional<Review> optionalReview = reviewRepository.findById(uniquePlaceUserReview);
        return optionalReview.isPresent();
    }

    public boolean isItFirstReviewForPlace(String placeId) {
        return !reviewRepository.reviewExistForPlace(placeId);
    }

    public boolean storeReviewsPhotosAndPoints(Review review, List<PointHistory> pointHistoryList) {
        List<ReviewPhoto> reviewPhotoList = new ArrayList<>();
        reviewRepository.save(review);

        review.getPhotoIdList().forEach(photoId -> {
            ReviewPhoto reviewPhoto = ReviewPhoto.builder()
                    .id(photoId)
                    .review(review)
                    .build();

            reviewPhotoList.add(reviewPhoto);

            if (reviewPhotoRepository.existsById(reviewPhoto.getId())) {
                throw new ReviewAlreadyExistException("The photoReview with id: " + reviewPhoto.getId() + " already exist");
            } else {
                reviewPhotoRepository.save(reviewPhoto);
            }

        });

        review.setAttachedPhotos(reviewPhotoList);
        pointService.storePointHistory(pointHistoryList);
        return true;
    }

    public Review getReview(UniquePlaceUserReview uniquePlaceUserReview) {
        Optional<Review> optionalReview = reviewRepository.findById(uniquePlaceUserReview);
        return optionalReview.orElseThrow(() -> new ReviewNotFoundException("No Review Found for PlaceID: "
                + uniquePlaceUserReview.getPlaceId() + " and UserID: " + uniquePlaceUserReview.getUserId()));

    }

    public void deleteReview(Review review) {
        reviewRepository.deleteById(review.getUniquePlaceUserReview());
    }

    public void deleteReviewPhoto(ReviewPhoto reviewPhoto) {
        reviewPhotoRepository.delete(reviewPhoto);
    }

}
