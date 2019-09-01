package io.github.rusyasoft.example.tour.elprit.elpritreview.service;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.place.exception.PlaceNotFoundException;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.point.model.PointHistory;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.model.User;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.place.model.Place;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.type.ActionType;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.place.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PointService pointService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    public Place getPlaceById(String placeId) {

        if (StringUtils.isEmpty(placeId)) {
            throw new PlaceNotFoundException("empty placeId has been provided!");
        }
        return placeRepository.findById(placeId).orElseThrow(() -> new PlaceNotFoundException("No place found with id: " + placeId));
    }

    @Transactional
    public User addReview(Review review) {
        List<PointHistory> pointHistoryList = pointService.calculatePoints(review, ActionType.ADD);
        reviewService.storeReviewsPhotosAndPoints(review, pointHistoryList);

        User user = userService.getUserById(review.getUniquePlaceUserReview().getUserId());
        return userService.accumulatePoint(user, sumOfPoints(pointHistoryList));
    }

    @Transactional
    public User modifyReview(Review review) {
        List<PointHistory> pointHistoryList = pointService.calculatePoints(review, ActionType.MOD);
        reviewService.storeReviewsPhotosAndPoints(review, pointHistoryList);

        User user = userService.getUserById(review.getUniquePlaceUserReview().getUserId());
        return userService.accumulatePoint(user, sumOfPoints(pointHistoryList));
    }

    @Transactional
    public User deleteReview(Review review) {
        List<PointHistory> pointHistoryList = pointService.calculatePoints(review, ActionType.DELETE);
        reviewService.storeReviewsPhotosAndPoints(review, pointHistoryList);

        // if the review text and images are all removed then remove the whole review
        if (StringUtils.isEmpty(review.getContent()) && review.getPhotoIdList().size() == 0) {
            reviewService.deleteReview(review);
        }
        pointService.storePointHistory(pointHistoryList);

        User user = userService.getUserById(review.getUniquePlaceUserReview().getUserId());
        return userService.accumulatePoint(user, sumOfPoints(pointHistoryList));
    }

    private int sumOfPoints(List<PointHistory> pointHistoryList) {
        return pointHistoryList.stream().map(PointHistory::getPoint).reduce(0, Integer::sum);
    }

}
