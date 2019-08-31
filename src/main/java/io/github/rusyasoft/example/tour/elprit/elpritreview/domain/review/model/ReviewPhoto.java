package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.review.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewPhoto {
    @Id
    private String id;

    @ManyToOne
    private Review review;
}
