package com.zdoryk.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record ContactDTO(

        @Pattern(
                regexp = "^380\\s\\d{2}\\s\\d{3}\\s\\d{4}$",
                message = "Invalid first phone number"
        )
        String firstPhoneNumber,

        @Pattern(
                regexp = "^380\\s\\d{2}\\s\\d{3}\\s\\d{4}$",
                message = "Invalid second phone number"
        )
        String secondPhoneNumber,

        @NotNull
        @NotBlank(message = "Please fill in all required fields.")
        @Email(message = "Invalid email format.")
        String email
) {
}
