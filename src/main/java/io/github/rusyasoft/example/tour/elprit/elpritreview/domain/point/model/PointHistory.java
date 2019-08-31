package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.point.model;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.type.ReviewPointType;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Builder
public class PointHistory {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column
    private int point; // can be + or -

    @Column
    private String reviewId;

    @Column
    private String description;

    public static PointHistory getPointHistory(Review review, ReviewPointType reviewPointType) {

        return PointHistory.builder()
                .point(reviewPointType.getPointVal())
                .description(reviewPointType.getCode())
                .reviewId(review.getId())
                .build();
    }
}
