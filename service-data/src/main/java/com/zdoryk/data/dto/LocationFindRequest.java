package com.zdoryk.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationFindRequest {

    @NotBlank(message = "City is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "City should contain only letters")
    private String city;

    @NotBlank(message = "Country is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Country should contain only letters")
    private String country;

}
