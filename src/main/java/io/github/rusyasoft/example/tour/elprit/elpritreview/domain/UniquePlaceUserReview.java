package io.github.rusyasoft.example.tour.elprit.elpritreview.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Builder
public class UniquePlaceUserReview implements Serializable {
    @Column
    private String placeId;

    @Column
    private String userId;
}
