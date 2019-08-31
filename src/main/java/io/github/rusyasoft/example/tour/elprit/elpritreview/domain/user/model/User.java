package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@JsonSerialize
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
public class User {

    @Id
    private String userId;

    @Column
    private String userName;

    @Column
    private int totalPoint;
}
