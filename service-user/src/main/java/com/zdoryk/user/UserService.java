package com.zdoryk.user;

import com.zdoryk.core.UserRequest;
import com.zdoryk.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void updateUser(UserRequest userRequest) {
        User user = userRepository.findUserByEmail(userRequest.email())
                .orElseThrow(() -> new NotFoundException("User does not exist"));

        if (userRequest.firstName() != null) {
            user.setFirstName(userRequest.firstName());
        }

        if (userRequest.secondName() != null) {
            user.setSecondName(userRequest.secondName());
        }

        if (userRequest.phoneNumber() != null) {
            user.setPhoneNumber(userRequest.phoneNumber());
        }
        userRepository.saveAndFlush(user);
    }
}
