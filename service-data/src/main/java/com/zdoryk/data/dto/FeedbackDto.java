package com.zdoryk.data.dto;

import com.zdoryk.data.core.NoHtmlTags;
import com.zdoryk.data.core.NoSQLInjection;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackDto implements Serializable  {

    @Email
    @NotBlank(message = "email should exist")
    @Size(max = 320,message = "max length 320")
    private String email;

    @NotBlank(message = "name must not be blank")
    @Size(min = 2,max = 30,message = "name length should be between 2 and 30 symbols")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯіІїЇєЄ\\s]+$", message = "should contain only letters")
    private String name;

    @NotBlank
    @Size(min = 1,max = 300,message = "message length min 1 max 300")
    @NoSQLInjection
    @NoHtmlTags
    private String message;
}
