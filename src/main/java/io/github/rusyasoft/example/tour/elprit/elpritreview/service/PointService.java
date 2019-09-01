package io.github.rusyasoft.example.tour.elprit.elpritreview.service;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.point.model.PointHistory;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.exception.ReviewAlreadyExistException;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.UniquePlaceUserReview;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.ReviewPhoto;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.type.ActionType;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.type.ReviewPointType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class PointService {

    @Autowired
    private PlaceService placeService;

    public List<PointHistory> calculatePoints(Review review, ActionType actionType) {

        switch (actionType) {
            case ADD: return calculateAddReviewPoints(review);
            case MOD: return calculateModifyReviewPoints(review);
            case DELETE: return calculateDeleteReviewPoints(review);
        }

        return null;
    }

    private List<PointHistory> calculateAddReviewPoints(Review review) {
        List<PointHistory> pointHistoryList = new ArrayList<>();

        // check the user has a review or not
        if (placeService.doesUserHasReviewForPlace(review.getUniquePlaceUserReview())) {
            throw new ReviewAlreadyExistException("Review By this User Already Exist");
        }

        if (StringUtils.hasText(review.getContent())) {
            pointHistoryList.add(PointHistory.getPointHistory(review, ReviewPointType.ADD_CONTENT));
        }

        if (placeService.isItFirstReviewForPlace(review.getUniquePlaceUserReview().getPlaceId())) {
            pointHistoryList.add(PointHistory.getPointHistory(review, ReviewPointType.ADD_FIRST_REVIEW));
            review.setFirstReview(true);
        }

        if (!CollectionUtils.isEmpty(review.getPhotoIdList())) {
            pointHistoryList.add(PointHistory.getPointHistory(review, ReviewPointType.ADD_PHOTO));
        }

        return pointHistoryList;
    }

    private List<PointHistory> calculateModifyReviewPoints(Review review) {
        UniquePlaceUserReview uniquePlaceUserReview = review.getUniquePlaceUserReview();
        Review beforeReview = placeService.getReview(uniquePlaceUserReview);

        List<PointHistory> pointHistoryList = new ArrayList<>();

        review.setFirstReview(beforeReview.getFirstReview());

        /// check the content
        if (StringUtils.isEmpty(beforeReview.getContent())) {
            // if the review didn't have any content before, now have
            if (StringUtils.hasText(review.getContent())) {
                pointHistoryList.add(PointHistory.getPointHistory(review, ReviewPointType.ADD_CONTENT));
            }
        } else {
            // if the review did have content before, now have removed it
            if (StringUtils.isEmpty(review.getContent())) {
                pointHistoryList.add(PointHistory.getPointHistory(review, ReviewPointType.DELETE_CONTENT));
                if (beforeReview.getFirstReview()) {
                    review.setFirstReview(false);
                    pointHistoryList.add(PointHistory.getPointHistory(review, ReviewPointType.DELETE_FIRST_REVIEW));
                }
            }
        }

        /// check the photos list
        if (CollectionUtils.isEmpty(beforeReview.getAttachedPhotos())) {
            // if the review didn't have any photos before, now have
            if (!CollectionUtils.isEmpty(review.getPhotoIdList())) {
                pointHistoryList.add(PointHistory.getPointHistory(review, ReviewPointType.ADD_PHOTO));
            }
        }

        return pointHistoryList;
    }

    private List<PointHistory> calculateDeleteReviewPoints(Review review) {
        UniquePlaceUserReview uniquePlaceUserReview = review.getUniquePlaceUserReview();
        Review beforeReview = placeService.getReview(uniquePlaceUserReview);

        List<PointHistory> pointHistoryList = new ArrayList<>();

        /// check the content
        if (StringUtils.hasText(beforeReview.getContent())) {
            // if the review did have content before, now have removed it
            if (StringUtils.isEmpty(review.getContent())) {
                pointHistoryList.add(PointHistory.getPointHistory(review, ReviewPointType.DELETE_CONTENT));
                beforeReview.setContent("");
                if (beforeReview.getFirstReview()) {
                    review.setFirstReview(false);
                    pointHistoryList.add(PointHistory.getPointHistory(review, ReviewPointType.DELETE_FIRST_REVIEW));
                }
            }
        }

        /// check the photos list
        if (!CollectionUtils.isEmpty(beforeReview.getAttachedPhotos())) {
            // if the review didn't have any photos before, now have

            List<ReviewPhoto> beforeReviewPhotoList = beforeReview.getAttachedPhotos();

            List<String> deleteReviewPhotoIds = review.getPhotoIdList();

            Iterator<ReviewPhoto> reviewPhotoIterator = beforeReviewPhotoList.iterator();
            while (reviewPhotoIterator.hasNext()) {
                ReviewPhoto reviewPhoto = reviewPhotoIterator.next();
                if (deleteReviewPhotoIds.contains(reviewPhoto.getId())) {
                    placeService.deleteReviewPhoto(reviewPhoto);
                    reviewPhotoIterator.remove();
                }
            }

            if (beforeReviewPhotoList.size() == 0) {
                pointHistoryList.add(PointHistory.getPointHistory(review, ReviewPointType.DELETE_PHOTO));

                // this is performed to tell higher level calls about no photos left in the DB
                review.setPhotoIdList(Collections.EMPTY_LIST);
            }
        }

        return pointHistoryList;
    }
}
