package com.zdoryk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class UserRegistrationRequest {


        @Email(message = "Invalid email format.")
        @NotBlank(message = "Please fill in all required fields.")
        @NotNull
        @JsonProperty("email") String email;

        @NotBlank(message = "Please fill in all required fields.")
        @Size(min = 8,message = "The password must be at least 8 characters" +
                " long and include numbers," +
                " uppercase and lowercase letters."
        )
        @NotNull
        @JsonProperty("password") String password;

}
