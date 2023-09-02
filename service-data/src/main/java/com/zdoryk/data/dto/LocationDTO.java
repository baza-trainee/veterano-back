package com.zdoryk.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO implements Serializable {

    @NotBlank(message = "City is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "City is required and should not contain numbers")
    private String city;

    @NotBlank(message = "Country is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Country is required and should not contain numbers")
    private String country;
}
