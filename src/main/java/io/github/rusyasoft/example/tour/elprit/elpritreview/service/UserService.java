package io.github.rusyasoft.example.tour.elprit.elpritreview.service;

import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.model.User;
import io.github.rusyasoft.example.tour.elprit.elpritreview.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(String userId) {

        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException("empty userId has been provided!");
        }

        try {
            return userRepository.getOne(userId);
        } catch (Exception ex) {
            throw new RuntimeException("Error happened while getting User by ID: " + userId);
        }

    }

    public User accumulatePoint(User user, int point) {
        user.setTotalPoint(point + user.getTotalPoint());
        userRepository.save(user);
        return user;
    }

}
