package com.zdoryk.user;

import com.zdoryk.core.ApiResponse;
import com.zdoryk.core.GenericController;
import com.zdoryk.core.UserRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users/credentials")
public class UserCredentialsController extends GenericController {

    private final UserService userService;


    @PutMapping("/add")
    public ResponseEntity<ApiResponse> updateUserCredentials(
            @Valid @RequestBody
            UserRequest userRequest){

        userService.updateUser(userRequest);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse(
                HttpStatus.ACCEPTED,
                Map.of("ACCEPTED","user has been updated")
        ));
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.findAll();
    }

}
