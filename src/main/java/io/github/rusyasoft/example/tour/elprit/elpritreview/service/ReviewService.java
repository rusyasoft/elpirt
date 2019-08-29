package io.github.rusyasoft.example.tour.elprit.elpritreview.service;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.*;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.dto.EventParam;
import io.github.rusyasoft.example.tour.elprit.elpritreview.repository.ReviewPhotoRepository;
import io.github.rusyasoft.example.tour.elprit.elpritreview.repository.ReviewRepository;
import io.github.rusyasoft.example.tour.elprit.elpritreview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewPhotoRepository reviewPhotoRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User addEvent(EventParam eventParam) {
        // check the user has a review or not
        if (doesUserHasReviewForPlace(eventParam)) {
            throw new RuntimeException("Review By this User Already Exist");
        }

        List<PointHistory> pointHistoryList = new ArrayList<>();

        boolean isItFirstReview = isItFirstReviewForPlace(eventParam);
        if (isItFirstReview) {
            pointHistoryList.add(generateFirstReviewPoint(eventParam));
        }

        if (StringUtils.hasText(eventParam.getContent())) {
            pointHistoryList.add(
                    PointHistory.builder()
                            .point(1)
                            .description("Review has a content")
                            .build()
            );
        }

        if (!CollectionUtils.isEmpty(eventParam.getAttachedPhotoIds())) {
            pointHistoryList.add(
                    PointHistory.builder()
                            .point(1)
                            .description("Review has a photos")
                            .build()
            );
        }


        UniquePlaceUserReview uniquePlaceUserReview = UniquePlaceUserReview.builder()
                .placeId(eventParam.getPlaceId())
                .userId(eventParam.getUserId())
                .build();

        Review review = Review.builder()
                .id(eventParam.getReviewId())
                .uniquePlaceUserReview(uniquePlaceUserReview)
                .content(eventParam.getContent())
                .firstReview(isItFirstReview)
                .build();

        reviewRepository.save(review);

        List<ReviewPhoto> reviewPhotoList = new ArrayList<>();
        eventParam.getAttachedPhotoIds().forEach(photoId -> {
            ReviewPhoto reviewPhoto = ReviewPhoto.builder()
                    .id(photoId)
                    .review(review)
                    .build();

            reviewPhotoList.add(reviewPhoto);

            // this save overwrites the photos
            if (reviewPhotoRepository.existsById(reviewPhoto.getId())) {
                throw new RuntimeException("The photoReview with id: " + reviewPhoto.getId() + " already exist");
            } else {
                reviewPhotoRepository.save(reviewPhoto);
            }

        });

        review.setAttachedPhotos(reviewPhotoList);


        /// point calculation
        int sumOfPoints = pointHistoryList.stream().map(PointHistory::getPoint).reduce(0, Integer::sum);

        User user = userRepository.getOne(eventParam.getUserId());
        user.setTotalPoint( user.getTotalPoint() + sumOfPoints);
        userRepository.save(user);


        return user;
    }

    public User modifyEvent(EventParam eventParam) {
        // check the user has a review or not

        UniquePlaceUserReview uniquePlaceUserReview = UniquePlaceUserReview.builder()
                .placeId(eventParam.getPlaceId())
                .userId(eventParam.getUserId())
                .build();

        Review beforeReview = reviewRepository.getOne(uniquePlaceUserReview);

        if (Objects.isNull(beforeReview)) {
            throw new RuntimeException("This user has no review on this place");
        }

        List<PointHistory> pointHistoryList = new ArrayList<>();


        /// check the content
        if (StringUtils.isEmpty(beforeReview.getContent())) {
            // if the review didn't have any content before, now have
            if (StringUtils.hasText(eventParam.getContent())) {
                pointHistoryList.add(
                        PointHistory.builder()
                                .point(1)
                                .description("Content has been added to Review")
                                .build()
                );
            }
        } else {
            // if the review did have content before, now have removed it
            if (StringUtils.isEmpty(eventParam.getContent())) {
                pointHistoryList.add(
                        PointHistory.builder()
                                .point(-1)
                                .description("Content has been removed from Review")
                                .build()
                );
            }
        }

        /// check the photos list
        if (CollectionUtils.isEmpty(beforeReview.getAttachedPhotos())) {
            // if the review didn't have any photos before, now have
            if (!CollectionUtils.isEmpty(eventParam.getAttachedPhotoIds())) {
                pointHistoryList.add(
                        PointHistory.builder()
                                .point(1)
                                .description("Photos were added to Review")
                                .build()
                );
            }
        }

        Review review = Review.builder()
                .id(eventParam.getReviewId())
                .uniquePlaceUserReview(uniquePlaceUserReview)
                .content(eventParam.getContent())
                .firstReview(false)  // <-- because either someone else was the number one or himself was number one
                .build();

        reviewRepository.save(review);

        List<ReviewPhoto> reviewPhotoList = new ArrayList<>();
        eventParam.getAttachedPhotoIds().forEach(photoId -> {
            ReviewPhoto reviewPhoto = ReviewPhoto.builder()
                    .id(photoId)
                    .review(review)
                    .build();

            reviewPhotoList.add(reviewPhoto);
            reviewPhotoRepository.save(reviewPhoto);
        });

        review.setAttachedPhotos(reviewPhotoList);


        /// point calculation
        int sumOfPoints = pointHistoryList.stream().map(PointHistory::getPoint).reduce(0, Integer::sum);

        User user = userRepository.getOne(eventParam.getUserId());
        user.setTotalPoint( user.getTotalPoint() + sumOfPoints);
        userRepository.save(user);


        return user;
    }


    public User deleteEvent(EventParam eventParam) {
        // check the user has a review or not
        UniquePlaceUserReview uniquePlaceUserReview = UniquePlaceUserReview.builder()
                .placeId(eventParam.getPlaceId())
                .userId(eventParam.getUserId())
                .build();

        Optional<Review> optinalBeforeReview = reviewRepository.findById(uniquePlaceUserReview);

        if (!optinalBeforeReview.isPresent()) {
            throw new RuntimeException("This user has no review on this place");
        }

        Review beforeReview = optinalBeforeReview.get();

        List<PointHistory> pointHistoryList = new ArrayList<>();

        /// check the content deletion
        if (StringUtils.hasText(beforeReview.getContent())) {
            // if the review did have content before, now have removed it
            if (StringUtils.isEmpty(eventParam.getContent())) {
                pointHistoryList.add(
                        PointHistory.builder()
                                .point(-1)
                                .description("Content has been removed from Review")
                                .build()
                );

                beforeReview.setContent("");
            }
        }

        /// check the photos list
        if (!CollectionUtils.isEmpty(beforeReview.getAttachedPhotos())) {
            // if the review didn't have any photos before, now have

            List<ReviewPhoto> beforeReviewPhotoList = beforeReview.getAttachedPhotos();

            List<String> deleteReviewPhotoIds = eventParam.getAttachedPhotoIds();

            Iterator<ReviewPhoto> reviewPhotoIterator = beforeReviewPhotoList.iterator();
            while (reviewPhotoIterator.hasNext()) {
                ReviewPhoto reviewPhoto = reviewPhotoIterator.next();
                if (deleteReviewPhotoIds.contains(reviewPhoto.getId())) {
                    reviewPhotoRepository.delete(reviewPhoto);
                    reviewPhotoIterator.remove();
                }
            }

            if (beforeReviewPhotoList.size() == 0) {
                pointHistoryList.add(
                        PointHistory.builder()
                                .point(-1)
                                .description("All Photos were removed to Review")
                                .build()
                );
            }
        }


        // if the review text and images are all removed then remove the whole review
        if (StringUtils.isEmpty(eventParam.getContent()) && beforeReview.getAttachedPhotos().size() == 0) {
//            Review review = Review.builder()
//                    .id(eventParam.getReviewId())
//                    .uniquePlaceUserReview(uniquePlaceUserReview)
//                    .content(eventParam.getContent())
//                    .build();

            reviewRepository.delete(beforeReview);
        } else {
            reviewRepository.save(beforeReview);
        }

        /// point calculation
        int sumOfPoints = pointHistoryList.stream().map(PointHistory::getPoint).reduce(0, Integer::sum);

        User user = userRepository.getOne(eventParam.getUserId());
        user.setTotalPoint( user.getTotalPoint() + sumOfPoints);
        userRepository.save(user);


        return user;
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

        return !reviewRepository.reviewExistForPlace(placeId);

    }

    private PointHistory generateFirstReviewPoint(EventParam eventParam) {
        PointHistory pointHistory = PointHistory.builder()
                .point(1)
                .description("First Review")
                .build();

        return pointHistory;
    }

}
