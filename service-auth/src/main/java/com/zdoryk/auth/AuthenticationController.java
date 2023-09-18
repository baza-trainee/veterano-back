package com.zdoryk.auth;

import com.zdoryk.core.AuthenticationResponse;
import com.zdoryk.core.GenericController;
import com.zdoryk.dto.UserLoginRequest;
import com.zdoryk.dto.UserRegistrationRequest;
import com.zdoryk.resetToken.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController extends GenericController {

    private final AuthenticationService authenticationService;
    private final PasswordResetService passwordResetService;


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody
            UserLoginRequest userLoginRequest){


            return ResponseEntity.ok(
                    Map.of(
                            "token",
                            authenticationService.login(userLoginRequest)
                    )
            );
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(
            @Valid @RequestBody
            UserRegistrationRequest userRegistrationRequest){

            return ResponseEntity.ok(Map.of(
               "token", authenticationService.register(userRegistrationRequest))
            );
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        log.info("Trying to validate link {}", token);
        return authenticationService.validateToken(token);
    }

    @GetMapping("/approve-token")
    public ResponseEntity<Void> redirectToOriginUrl(
            @RequestParam("token") String token
    ){
        return ResponseEntity
                .status(302)
                .location(authenticationService.confirmToken(token))
                .build();
    }

    @GetMapping("/password-reset-request")
    public ResponseEntity<?> requestResetPassword(
            @RequestParam("email") String email){

        passwordResetService.generatePasswordResetToken(email);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/password-reset")
    public ResponseEntity<?> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String newPassword
    ) {
        passwordResetService.resetPassword(token,newPassword);
        return ResponseEntity.status(200).build();
    }


    @PostMapping("/refresh-token")
    public AuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        return authenticationService.refreshToken(request,response);
    }



    @GetMapping
    public List<User> getAllUsers(){
        return authenticationService.findAllUsers();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping(){
        return ResponseEntity.ok().build();
    }


}
