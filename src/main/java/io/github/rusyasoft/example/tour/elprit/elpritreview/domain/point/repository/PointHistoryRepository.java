package io.github.rusyasoft.example.tour.elprit.elpritreview.domain.point.repository;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.point.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer> {
}
