package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.place.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Place {
    @Id
    private String id;

    @Column
    private String name;
}
