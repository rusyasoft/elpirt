package io.github.rusyasoft.example.tour.elprit.elpritreview.service;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.model.User;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.dto.EventParam;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.type.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class EventService {



    @Autowired
    private UserService userService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ReviewService reviewService;

    @Transactional
    public User addEvent(EventParam eventParam) {


        if (eventParam.getType() == EventType.REVIEW) {
            return reviewService.processReviewEvent(eventParam);
        }

        return null;
    }

//    public User modifyEvent(EventParam eventParam) {
//        // check the user has a review or not
//
//        UniquePlaceUserReview uniquePlaceUserReview = UniquePlaceUserReview.builder()
//                .placeId(eventParam.getPlaceId())
//                .userId(eventParam.getUserId())
//                .build();
//
//        Review beforeReview = reviewRepository.getOne(uniquePlaceUserReview);
//
//        if (Objects.isNull(beforeReview)) {
//            throw new RuntimeException("This user has no review on this place");
//        }
//
//        List<PointHistory> pointHistoryList = new ArrayList<>();
//
//
//        /// check the content
//        if (StringUtils.isEmpty(beforeReview.getContent())) {
//            // if the review didn't have any content before, now have
//            if (StringUtils.hasText(eventParam.getContent())) {
//                pointHistoryList.add(
//                        PointHistory.builder()
//                                .point(1)
//                                .description("Content has been added to Review")
//                                .build()
//                );
//            }
//        } else {
//            // if the review did have content before, now have removed it
//            if (StringUtils.isEmpty(eventParam.getContent())) {
//                pointHistoryList.add(
//                        PointHistory.builder()
//                                .point(-1)
//                                .description("Content has been removed from Review")
//                                .build()
//                );
//            }
//        }
//
//        /// check the photos list
//        if (CollectionUtils.isEmpty(beforeReview.getAttachedPhotos())) {
//            // if the review didn't have any photos before, now have
//            if (!CollectionUtils.isEmpty(eventParam.getAttachedPhotoIds())) {
//                pointHistoryList.add(
//                        PointHistory.builder()
//                                .point(1)
//                                .description("Photos were added to Review")
//                                .build()
//                );
//            }
//        }
//
//        Review review = Review.builder()
//                .id(eventParam.getReviewId())
//                .uniquePlaceUserReview(uniquePlaceUserReview)
//                .content(eventParam.getContent())
//                .firstReview(false)  // <-- because either someone else was the number one or himself was number one
//                .build();
//
//        reviewRepository.save(review);
//
//        List<ReviewPhoto> reviewPhotoList = new ArrayList<>();
//        eventParam.getAttachedPhotoIds().forEach(photoId -> {
//            ReviewPhoto reviewPhoto = ReviewPhoto.builder()
//                    .id(photoId)
//                    .review(review)
//                    .build();
//
//            reviewPhotoList.add(reviewPhoto);
//            reviewPhotoRepository.save(reviewPhoto);
//        });
//
//        review.setAttachedPhotos(reviewPhotoList);
//
//
//        /// point calculation
//        int sumOfPoints = pointHistoryList.stream().map(PointHistory::getPoint).reduce(0, Integer::sum);
//
//        User user = userRepository.getOne(eventParam.getUserId());
//        user.setTotalPoint( user.getTotalPoint() + sumOfPoints);
//        userRepository.save(user);
//
//
//        return user;
//    }
//
//
//    public User deleteEvent(EventParam eventParam) {
//        // check the user has a review or not
//        UniquePlaceUserReview uniquePlaceUserReview = UniquePlaceUserReview.builder()
//                .placeId(eventParam.getPlaceId())
//                .userId(eventParam.getUserId())
//                .build();
//
//        Optional<Review> optinalBeforeReview = reviewRepository.findById(uniquePlaceUserReview);
//
//        if (!optinalBeforeReview.isPresent()) {
//            throw new RuntimeException("This user has no review on this place");
//        }
//
//        Review beforeReview = optinalBeforeReview.get();
//
//        List<PointHistory> pointHistoryList = new ArrayList<>();
//
//        /// check the content deletion
//        if (StringUtils.hasText(beforeReview.getContent())) {
//            // if the review did have content before, now have removed it
//            if (StringUtils.isEmpty(eventParam.getContent())) {
//                pointHistoryList.add(
//                        PointHistory.builder()
//                                .point(-1)
//                                .description("Content has been removed from Review")
//                                .build()
//                );
//
//                beforeReview.setContent("");
//            }
//        }
//
//        /// check the photos list
//        if (!CollectionUtils.isEmpty(beforeReview.getAttachedPhotos())) {
//            // if the review didn't have any photos before, now have
//
//            List<ReviewPhoto> beforeReviewPhotoList = beforeReview.getAttachedPhotos();
//
//            List<String> deleteReviewPhotoIds = eventParam.getAttachedPhotoIds();
//
//            Iterator<ReviewPhoto> reviewPhotoIterator = beforeReviewPhotoList.iterator();
//            while (reviewPhotoIterator.hasNext()) {
//                ReviewPhoto reviewPhoto = reviewPhotoIterator.next();
//                if (deleteReviewPhotoIds.contains(reviewPhoto.getId())) {
//                    reviewPhotoRepository.delete(reviewPhoto);
//                    reviewPhotoIterator.remove();
//                }
//            }
//
//            if (beforeReviewPhotoList.size() == 0) {
//                pointHistoryList.add(
//                        PointHistory.builder()
//                                .point(-1)
//                                .description("All Photos were removed to Review")
//                                .build()
//                );
//            }
//        }
//
//
//        // if the review text and images are all removed then remove the whole review
//        if (StringUtils.isEmpty(eventParam.getContent()) && beforeReview.getAttachedPhotos().size() == 0) {
////            Review review = Review.builder()
////                    .id(eventParam.getReviewId())
////                    .uniquePlaceUserReview(uniquePlaceUserReview)
////                    .content(eventParam.getContent())
////                    .build();
//
//            reviewRepository.delete(beforeReview);
//        } else {
//            reviewRepository.save(beforeReview);
//        }
//
//        /// point calculation
//        int sumOfPoints = pointHistoryList.stream().map(PointHistory::getPoint).reduce(0, Integer::sum);
//
//        User user = userRepository.getOne(eventParam.getUserId());
//        user.setTotalPoint( user.getTotalPoint() + sumOfPoints);
//        userRepository.save(user);
//
//
//        return user;
//    }







}
