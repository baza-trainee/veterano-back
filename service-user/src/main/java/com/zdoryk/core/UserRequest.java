package com.zdoryk.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UserRequest(

        @Size(min = 2, max = 40, message = "First name length should be between 2 and 40 characters")
        @JsonProperty(value = "firstName") String firstName,
        @Size(min = 2, max = 40, message = "First name length should be between 2 and 40 characters")
        @JsonProperty(value = "secondName") String secondName,
        @Email @JsonProperty(value = "email") String email,

        @Pattern(regexp = "^\\+?380\\d{9}$", message = "Invalid phone number")
        String phoneNumber,
        @JsonProperty(value = "userId")UUID id)
{}
