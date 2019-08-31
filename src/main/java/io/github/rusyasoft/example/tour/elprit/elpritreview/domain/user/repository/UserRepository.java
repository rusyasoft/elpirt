package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.repository;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
