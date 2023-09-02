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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/auth")
@RequiredArgsConstructor
@Slf4j
public class UserAuthController extends GenericController {

    private final UserAuthService userAuthService;
    private final PasswordResetService passwordResetService;


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody
            UserLoginRequest userLoginRequest){


            return ResponseEntity.ok(
                    Map.of(
                            "token",
                            userAuthService.login(userLoginRequest)
                    )
            );
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(
            @Valid @RequestBody
            UserRegistrationRequest userRegistrationRequest){

            return ResponseEntity.ok(Map.of(
               "token",userAuthService.registerUser(userRegistrationRequest))
            );
    }
    @GetMapping("/approve-token")
    public void approveToken(@RequestParam("token") String token){
            userAuthService.confirmToken(token);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<UserDto> validateToken(@RequestParam String token) {
        log.info("Trying to validate link {}", token);
        return ResponseEntity.ok(userAuthService.validateToken(token));
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


    @GetMapping
    public List<User> getAllUsers(){
        return userAuthService.findAllUsers();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping(){
        return ResponseEntity.ok().build();
    }


}
