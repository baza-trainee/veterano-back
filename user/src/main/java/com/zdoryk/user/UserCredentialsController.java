package com.zdoryk.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users/credentials")
public class UserCredentialsController {

    private final UserService userService;


    @GetMapping
    public List<User> getAllUsers(){
        return userService.findAll();
    }

}
