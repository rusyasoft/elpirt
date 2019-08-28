package io.github.rusyasoft.example.tour.elprit.elpritreview.domain;

import javax.persistence.*;
import javax.swing.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Review implements Serializable {

    @Column(unique = true)
    private String id;

    @Column
    private String content;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewPhoto> attachedPhotos;

    @EmbeddedId
    private UniquePlaceUserReview uniquePlaceUserReview;
}
