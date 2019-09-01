package io.github.rusyasoft.example.tour.elprit.elpritreview.service;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.place.exception.PlaceNotFoundException;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.point.model.PointHistory;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.exception.ReviewAlreadyExistException;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.exception.ReviewNotFoundException;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.UniquePlaceUserReview;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.model.User;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.place.model.Place;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.ReviewPhoto;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.repository.ReviewPhotoRepository;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.repository.ReviewRepository;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.type.ActionType;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.place.repository.PlaceRepository;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.point.repository.PointHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewPhotoRepository reviewPhotoRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private PointService pointService;

    @Autowired
    private UserService userService;

    public Place getPlaceById(String placeId) {

        if (StringUtils.isEmpty(placeId)) {
            throw new PlaceNotFoundException("empty placeId has been provided!");
        }

        return placeRepository.findById(placeId).orElseThrow(() -> new PlaceNotFoundException("No place found with id: " + placeId));

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

    @Transactional
    public User addReview(Review review) {

        List<PointHistory> pointHistoryList = pointService.calculatePoints(review, ActionType.ADD);
        storeReviewsPhotosAndPoints(review, pointHistoryList);

        /// point calculation
        int sumOfPoints = pointHistoryList.stream().map(PointHistory::getPoint).reduce(0, Integer::sum);

        User user = userService.getUserById(review.getUniquePlaceUserReview().getUserId());
        return userService.accumulatePoint(user, sumOfPoints);
    }

    @Transactional
    public User modifyReview(Review review) {
        List<PointHistory> pointHistoryList = pointService.calculatePoints(review, ActionType.MOD);
        storeReviewsPhotosAndPoints(review, pointHistoryList);

        /// point calculation
        int sumOfPoints = pointHistoryList.stream().map(PointHistory::getPoint).reduce(0, Integer::sum);

        User user = userService.getUserById(review.getUniquePlaceUserReview().getUserId());
        return userService.accumulatePoint(user, sumOfPoints);
    }

    @Transactional
    public User deleteReview(Review review) {

        List<PointHistory> pointHistoryList = pointService.calculatePoints(review, ActionType.DELETE);
        storeReviewsPhotosAndPoints(review, pointHistoryList);

        // if the review text and images are all removed then remove the whole review
        if (StringUtils.isEmpty(review.getContent()) && review.getPhotoIdList().size() == 0) {
            reviewRepository.deleteById(review.getUniquePlaceUserReview());
        }

        storePointHistory(pointHistoryList);

        /// point calculation
        int sumOfPoints = pointHistoryList.stream().map(PointHistory::getPoint).reduce(0, Integer::sum);

        User user = userService.getUserById(review.getUniquePlaceUserReview().getUserId());
        return userService.accumulatePoint(user, sumOfPoints);

    }

    private boolean storeReviewsPhotosAndPoints(Review review, List<PointHistory> pointHistoryList) {

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


        storePointHistory(pointHistoryList);

        return true;
    }

    private boolean storePointHistory(List<PointHistory> pointHistoryList) {

        pointHistoryList.stream().forEach(pointHistory -> {
            pointHistoryRepository.save(pointHistory);
        });

        return true;
    }

    //TODO: should we move this review getter into the ReviewService ???
    public Review getReview(UniquePlaceUserReview uniquePlaceUserReview) {
        Optional<Review> optionalReview = reviewRepository.findById(uniquePlaceUserReview);
        return optionalReview.orElseThrow(() -> new ReviewNotFoundException("No Review Found for PlaceID: "
                + uniquePlaceUserReview.getPlaceId() + " and UserID: " + uniquePlaceUserReview.getUserId()));

    }

    public void deleteReviewPhoto(ReviewPhoto reviewPhoto) {
        reviewPhotoRepository.delete(reviewPhoto);
    }

}
