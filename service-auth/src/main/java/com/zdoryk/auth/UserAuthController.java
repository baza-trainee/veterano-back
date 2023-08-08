package com.zdoryk.auth;

import com.zdoryk.core.*;
import com.zdoryk.dto.UserDto;
import com.zdoryk.dto.UserLoginRequest;
import com.zdoryk.dto.UserRegistrationRequest;
import com.zdoryk.exceptions.RateLimitExceededException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
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
    private final Bucket bucket = Bucket4j.builder()
            .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1))))
            .build();

    @PostMapping("/login")
    public ResponseEntity<APICustomResponse> login(
            @Valid @RequestBody
            UserLoginRequest userLoginRequest){


        if(bucket.tryConsume(1L)) {
            return createResponse(
                    Map.of("user", userAuthService.login(userLoginRequest)),
                    "user",
                    HttpStatus.OK
            );
        }

        throw new RateLimitExceededException("The speed limit has been exceeded." +
                " Please try again later.");
    }

    @PostMapping("/register")
    public ResponseEntity<APICustomResponse> saveUser(
            @Valid @RequestBody
            UserRegistrationRequest userRegistrationRequest){

        if(bucket.tryConsume(1L)){
            return createResponse(
                    Map.of("token", userAuthService.registerUser(userRegistrationRequest)),
                    "User registered, please activate account",
                    HttpStatus.OK
            );
        }


        throw new RateLimitExceededException("The speed limit has been exceeded." +
                                            " Please try again later.");
    }
    @GetMapping("/approve-token")
    public void approveToken(@RequestParam("token") String token){

        if(bucket.tryConsume(1L)) {
            userAuthService.confirmToken(token);
        }

        throw new RateLimitExceededException("The speed limit has been exceeded." +
                " Please try again later.");
    }

    @PostMapping("/validate-token")
    public ResponseEntity<UserDto> validateToken(@RequestParam String token) {
        log.info("Trying to validate token {}", token);
        return ResponseEntity.ok(userAuthService.validateToken(token));
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
