package com.zdoryk.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FeedbackDto {

    @Email
    private String email;

    private String name;

    private String phoneNumber;

    @NotBlank
    private String message;

}
