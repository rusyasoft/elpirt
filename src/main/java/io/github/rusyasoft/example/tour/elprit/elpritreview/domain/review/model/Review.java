package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model;

import io.github.rusyasoft.example.tour.elprit.elpritreview.model.UniquePlaceUserReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review implements Serializable {

    @Column(unique = true)
    private String id;

    @Column
    private String content;

    @OneToMany(mappedBy = "review") //, cascade = CascadeType.ALL)
    private List<ReviewPhoto> attachedPhotos;

    @Transient
    private List<String> photoIdList;

    @EmbeddedId
    private UniquePlaceUserReview uniquePlaceUserReview;

    @Column(nullable = false)
    private Boolean firstReview;

//    @ManyToOne
//    private Place place;

}
