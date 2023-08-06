package com.zdoryk.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserLoginRequest(
        @Email(message = "Invalid email format.")
        @NotBlank(message = "Please fill in all required fields.")
        @NotNull(message = "Please provide email")
        @JsonProperty("email") String email,
        @NotBlank(message = "Please fill in all required fields.")
        @Min(value = 8,message = "The password must be at least 8 characters" +
                " long and include numbers," +
                " uppercase and lowercase letters."
        )
        @NotNull(message = "Please provide password")
        @JsonProperty("password") String password) {
}
