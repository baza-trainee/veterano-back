package com.zdoryk.auth;
import com.zdoryk.exceptions.IncorrectDataException;
import com.zdoryk.exceptions.NotFoundException;
import com.zdoryk.exceptions.ResourceExistsException;
import com.zdoryk.dto.UserDto;
import com.zdoryk.dto.UserLoginRequest;
import com.zdoryk.dto.UserRegistrationRequest;
import com.zdoryk.confirmationToken.ConfirmationService;
import com.zdoryk.confirmationToken.ConfirmationToken;
import com.zdoryk.util.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class UserAuthService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationService confirmationService;

    public String login(UserLoginRequest userLoginRequest){

        User user = userRepository.findUserByEmail(userLoginRequest.email())
                .orElseThrow(() -> new NotFoundException("User does not exist"));

        if(!user.getEnabled()){
            throw new IncorrectDataException("User with these data didn't activated account yet");
        }
        if (!passwordEncoder.matches(userLoginRequest.password(), user.getPassword())) {
            throw new IncorrectDataException("Incorrect password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }


    @Transactional
    public String registerUser(UserRegistrationRequest userRegistrationRequest) {
        String email = userRegistrationRequest.getEmail();

        if (userRepository.existsUserByEmail(email)) {
            throw new ResourceExistsException("User with email " + email + " already exists");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .enabled(false)
                .userRole(UserRole.USER)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(email);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                email,
                user.getUserId()
        );

        confirmationService.saveConfirmationToken(confirmationToken);
        confirmationService.sendEmailConfirmation(email, token);

        return token;
    }




    public UserDto validateToken(String token) {
        String email = jwtUtil.validateTokenAndRetrieveClaim(token);
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return   UserDto.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .build();
    }


    @Transactional
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationService
                .getToken(token)
                .orElseThrow(() -> new NotFoundException("Token not found"));

        LocalDateTime confirmedAt = confirmationToken.getConfirmedAt();
        if (confirmedAt != null) {
            throw new ResourceExistsException("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ResourceExistsException("Token expired");
        }

        confirmationService.setConfirmedAt(token);
        userRepository.enableAppUser(confirmationToken.getEmail());
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
