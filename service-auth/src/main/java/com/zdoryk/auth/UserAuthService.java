package com.zdoryk.auth;
import com.zdoryk.exceptions.IncorrectDataException;
import com.zdoryk.exceptions.NotFoundException;
import com.zdoryk.exceptions.ResourceExistsException;
import com.zdoryk.core.UserDto;
import com.zdoryk.core.UserLoginRequest;
import com.zdoryk.core.UserRegistrationRequest;
import com.zdoryk.exceptions.UserValidationException;
import com.zdoryk.token.ConfirmationService;
import com.zdoryk.token.ConfirmationToken;
import com.zdoryk.util.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserAuthService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationService confirmationService;

    public String login(UserLoginRequest userLoginRequest){

        Optional<User> optionalUser = userRepository.findUserByEmail(userLoginRequest.email());

        if(optionalUser.isEmpty()){
            throw new NotFoundException("User not found");
        }

        User user = optionalUser.get();
        if(!user.getEnabled()){
            throw new IncorrectDataException("User with these data didn't activated account yet");
        }
        if (!passwordEncoder.matches(userLoginRequest.password(), user.getPassword())) {
            throw new IncorrectDataException("Incorrect password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }


    @Transactional()
    public String registerUser(UserRegistrationRequest userRegistrationRequest) {


        userRepository.findUserByEmail(userRegistrationRequest.getEmail())
                .ifPresent(x -> {
                    throw new ResourceExistsException(
                            "user with email " +
                            userRegistrationRequest.getEmail()
                            + " exists");
                });
        User user = new User().builder()
                .email(userRegistrationRequest.getEmail())
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .enabled(false)
                .userRole(UserRole.USER)
                .build();

        String email = userRegistrationRequest.getEmail();
        String token = jwtUtil.generateToken(user.getEmail());
        userRepository.saveAndFlush(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                email,
                userRepository.findUserByEmail(userRegistrationRequest
                        .getEmail())
                        .get()
                        .getUserId()
        );

        confirmationService.saveConfirmationToken(confirmationToken);
        confirmationService.sendEmailConfirmation(
                email,
                token
        );
        return token;
    }



    public UserDto validateToken(String token) {
        String email = jwtUtil.validateTokenAndRetrieveClaim(token);
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();
        return new UserDto().builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationService
                .getToken(token)
                .orElseThrow(() ->
                        new NotFoundException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new ResourceExistsException("email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ResourceExistsException("token expired");
        }

        confirmationService.setConfirmedAt(token);
        userRepository.enableAppUser(
                confirmationToken.getEmail());
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
