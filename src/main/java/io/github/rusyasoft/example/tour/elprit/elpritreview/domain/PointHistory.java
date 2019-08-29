package io.github.rusyasoft.example.tour.elprit.elpritreview.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
@Builder
public class PointHistory {
    @Id
    private String id;

    @Column
    private int point; // can be + or -

    @ManyToOne
    private Review review;

    @Column
    private String description;

}
