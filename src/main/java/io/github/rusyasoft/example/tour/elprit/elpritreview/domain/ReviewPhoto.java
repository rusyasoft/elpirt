package io.github.rusyasoft.example.tour.elprit.elpritreview.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

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
