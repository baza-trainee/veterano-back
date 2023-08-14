package com.zdoryk.auth;

import com.zdoryk.core.APICustomResponse;
import com.zdoryk.core.GenericController;
import com.zdoryk.dto.UserDto;
import com.zdoryk.dto.UserLoginRequest;
import com.zdoryk.dto.UserRegistrationRequest;
import com.zdoryk.resetToken.PasswordResetRequest;
import com.zdoryk.resetToken.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/auth")
@RequiredArgsConstructor
@Slf4j
public class UserAuthController extends GenericController {

    private final UserAuthService userAuthService;
    private final PasswordResetService passwordResetService;
//    private final Bucket bucket = Bucket4j.builder()
//            .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1))))
//            .build();

    @PostMapping("/login")
    public ResponseEntity<APICustomResponse> login(
            @Valid @RequestBody
            UserLoginRequest userLoginRequest){


//        if(bucket.tryConsume(1L)) {
            return createResponse(
                    Map.of("user", userAuthService.login(userLoginRequest)),
                    "user",
                    HttpStatus.OK
            );
//        }
//
//        throw new RateLimitExceededException("The speed limit has been exceeded." +
//                " Please try again later.");
    }

    @PostMapping("/register")
    public ResponseEntity<APICustomResponse> saveUser(
            @Valid @RequestBody
            UserRegistrationRequest userRegistrationRequest){

            return createResponse(
                    Map.of("link", userAuthService.registerUser(userRegistrationRequest)),
                    "User registered, please activate account",
                    HttpStatus.OK
            );
    }
    @GetMapping("/approve-token")
    public void approveToken(@RequestParam("token") String token){

//        if(bucket.tryConsume(1L)) {
            userAuthService.confirmToken(token);
//        }
//
//        throw new RateLimitExceededException("The speed limit has been exceeded." +
//                " Please try again later.");
    }

    @PostMapping("/validate-token")
    public ResponseEntity<UserDto> validateToken(@RequestParam String token) {
        log.info("Trying to validate link {}", token);
        return ResponseEntity.ok(userAuthService.validateToken(token));
    }


    @PostMapping("/password-reset-request")
    public ResponseEntity<APICustomResponse> requestResetPassword(
            @RequestBody PasswordResetRequest request){

        passwordResetService.generatePasswordResetToken(request.email());
        return createResponse(
                Map.of("USER","email"),
                "success",
                HttpStatus.OK
        );
    }

    @PostMapping("/password-reset")
    public ResponseEntity<APICustomResponse> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String newPassword
    ) {
        passwordResetService.resetPassword(token,newPassword);
        return createResponse(
                Map.of("USER","email"),
                "success",
                HttpStatus.OK
        );
    }


    @GetMapping
    public List<User> getAllUsers(){
        return userAuthService.findAllUsers();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping(){
        return ResponseEntity.ok().build();
    }


}
