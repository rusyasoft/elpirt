package io.github.rusyasoft.example.tour.elprit.elpritreview.service;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.model.User;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.dto.EventParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class EventService {

    @Autowired
    private ReviewService reviewService;

    @Transactional
    public User onEvent(EventParam eventParam) {
        switch (eventParam.getType()) {
            case REVIEW: return reviewService.processReviewEvent(eventParam);
            case NONE: return null;
        }

        return null;
    }
}
