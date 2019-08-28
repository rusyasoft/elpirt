package io.github.rusyasoft.example.tour.elprit.elpritreview.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    private String userId;

    @Column
    private String userName;

}
