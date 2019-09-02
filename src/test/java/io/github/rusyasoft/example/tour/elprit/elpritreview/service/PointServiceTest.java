package io.github.rusyasoft.example.tour.elprit.elpritreview.service;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.point.model.PointHistory;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.ReviewPhoto;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.UniquePlaceUserReview;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.type.ActionType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.sampled.ReverbType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PointServiceTest {

    public final String [] PHOTO_ID_LIST = {"photo_id_1", "photo_id_2", "photo_id_3", "photo_id_4"};

    @Autowired
    private PointService pointService;

    @MockBean
    private ReviewService mockedReviewservice;

    private void beforeAdd(UniquePlaceUserReview uniquePlaceUserReview) {
        when(mockedReviewservice.isItFirstReviewForPlace(uniquePlaceUserReview.getPlaceId()))
                .thenReturn(true);
    }

    private void beforeMod(UniquePlaceUserReview uniquePlaceUserReview) {
        Review addedReview = Review.builder()
                .firstReview(true)
                .uniquePlaceUserReview(uniquePlaceUserReview)
                .content("")
                .id("test_review_id_1")
                .photoIdList(Collections.EMPTY_LIST)
                .build();

        List<ReviewPhoto> attachedPhotos = new ArrayList<>();
        for (String photoId : PHOTO_ID_LIST) {
            attachedPhotos.add(ReviewPhoto.builder()
                    .review(addedReview)
                    .id(photoId)
                    .build()
            );
        }
        addedReview.setAttachedPhotos(attachedPhotos);


        when(mockedReviewservice.getReview(uniquePlaceUserReview))
                .thenReturn(addedReview);
    }

    private void beforeDelete(UniquePlaceUserReview uniquePlaceUserReview) {
        Review addedReview = Review.builder()
                .firstReview(true)
                .uniquePlaceUserReview(uniquePlaceUserReview)
                .content("there is some content")
                .id("test_review_id_1")
                .photoIdList(Collections.EMPTY_LIST)
                .build();
        addedReview.setAttachedPhotos(generateAttachedPhotoList(addedReview));

        when(mockedReviewservice.getReview(uniquePlaceUserReview))
                .thenReturn(addedReview);
    }

    private List<ReviewPhoto> generateAttachedPhotoList(Review review) {
        List<ReviewPhoto> attachedPhotos = new ArrayList<>();
        for (String photoId : PHOTO_ID_LIST) {
            attachedPhotos.add(ReviewPhoto.builder()
                    .review(review)
                    .id(photoId)
                    .build()
            );
        }
        return attachedPhotos;
    }

    @Test
    public void calculatePointsTest() {

        UniquePlaceUserReview uniquePlaceUserReview = UniquePlaceUserReview.builder()
                .placeId("test_place_id_1")
                .userId("test_user_id_1")
                .build();

        Review addReview = Review.builder()
                .firstReview(false)
                .uniquePlaceUserReview(uniquePlaceUserReview)
                .content("")
                .id("test_review_id_1")
                .photoIdList(Arrays.asList(PHOTO_ID_LIST))
                .build();

        beforeAdd(uniquePlaceUserReview);

        List<PointHistory> pointHistoryList = pointService.calculatePoints(addReview, ActionType.ADD);

        Assert.assertEquals(2, pointHistoryList.size());
        Assert.assertEquals("ADD_FIRST_REVIEW", pointHistoryList.get(0).getDescription());
        Assert.assertEquals("ADD_PHOTO", pointHistoryList.get(1).getDescription());

        Review modReview = Review.builder()
                .firstReview(false)
                .uniquePlaceUserReview(uniquePlaceUserReview)
                .content("Now we decided to add the content")
                .id("test_review_id_1")
                .photoIdList(Collections.EMPTY_LIST)
                .build();

        beforeMod(uniquePlaceUserReview);
        pointHistoryList = pointService.calculatePoints(modReview, ActionType.MOD);

        Assert.assertEquals(1, pointHistoryList.size());
        Assert.assertEquals("ADD_CONTENT", pointHistoryList.get(0).getDescription());


        beforeDelete(uniquePlaceUserReview);

        pointHistoryList = pointService.calculatePoints(addReview, ActionType.DELETE);

        Assert.assertEquals(3, pointHistoryList.size());
        Assert.assertEquals("DELETE_CONTENT", pointHistoryList.get(0).getDescription());
        Assert.assertEquals("DELETE_FIRST_REVIEW", pointHistoryList.get(1).getDescription());
        Assert.assertEquals("DELETE_PHOTO", pointHistoryList.get(2).getDescription());

    }

}
