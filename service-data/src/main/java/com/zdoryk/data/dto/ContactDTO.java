package com.zdoryk.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record ContactDTO(

        @Pattern(
                regexp = "^\\+?380\\d{9}$",
                message = "Invalid first phone number"
        )
        String firstPhoneNumber,

        @Pattern(
                regexp = "^\\+?380\\d{9}$",
                message = "Invalid second phone number"
        )
        String secondPhoneNumber,

        @NotNull
        @NotBlank(message = "Please fill in all required fields.")
        @Email(message = "Invalid email format.")
        String email
) {
}
