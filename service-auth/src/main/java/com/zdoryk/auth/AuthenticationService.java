package com.zdoryk.auth;
import com.zdoryk.core.AuthenticationResponse;
import com.zdoryk.exceptions.*;
import com.zdoryk.dto.UserLoginRequest;
import com.zdoryk.dto.UserRegistrationRequest;
import com.zdoryk.confirmationToken.ConfirmationService;
import com.zdoryk.confirmationToken.ConfirmationToken;
import com.zdoryk.util.JWTUtil;
import com.zdoryk.util.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationService confirmationService;

    public AuthenticationResponse login(UserLoginRequest userLoginRequest){

        if(Utils.isValidEmail(userLoginRequest.email())){
            throw new IncorrectDataException("Email is not valid");
        }

        User user = userRepository.findUserByEmail(userLoginRequest.email())
                .orElseThrow(() -> new NotFoundException("User does not exist"));

        if(!user.getEnabled()){
            throw new IncorrectDataException("User with these data didn't activated account yet");
        }
        if (!passwordEncoder.matches(userLoginRequest.password(), user.getPassword())) {
            throw new IncorrectDataException("Incorrect data");
        }
        var jwtToken = jwtUtil.generateToken(user.getEmail());
        var refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }


    @Transactional
    public AuthenticationResponse register(UserRegistrationRequest userRegistrationRequest) {

        if(Utils.isValidEmail(userRegistrationRequest.getEmail())){
            throw new IncorrectDataException("Email is not valid");
        }

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

        var jwtToken = jwtUtil.generateToken(email);
        var refreshToken = jwtUtil.generateRefreshToken(email);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                jwtToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                email,
                user.getUserId()
        );

        confirmationService.saveConfirmationToken(confirmationToken);
        confirmationService.sendEmailConfirmation(email, jwtToken);



        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }




    public ResponseEntity<Boolean> validateToken(String token) {

        String email = jwtUtil.validateTokenAndRetrieveClaim(token);
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return ResponseEntity.ok(user != null);
    }


    @SneakyThrows
    @Transactional
    public URI confirmToken(String token) {
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
        return new URI("https://hyst.site/");
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new EmptyFieldException("Header is Null");
        }
        refreshToken = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndRetrieveClaim(refreshToken);
        if(email != null){
            var user = userRepository.findUserByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User Not Found"));
            if(!jwtUtil.isTokenExpired(refreshToken)){
                var accessToken = jwtUtil.generateToken(email);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }

        }
        throw new UserValidationException("Token cant be refreshed");
    }
}
