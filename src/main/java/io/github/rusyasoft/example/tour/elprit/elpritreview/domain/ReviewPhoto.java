package io.github.rusyasoft.example.tour.elprit.elpritreview.domain;

import javax.persistence.*;

@Entity
public class ReviewPhoto {
    @Id
    private String id;

    @ManyToOne
    private Review review;

    @Column
    private String name;
}
