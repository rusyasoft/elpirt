package io.github.rusyasoft.example.tour.elprit.elpritreview.repository;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.Review;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.UniquePlaceUserReview;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {
}
