package com.zdoryk.data.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;


@Data
public class CardSaveRequest {

    @Schema(name = "description",example = "text for card")
    @NotBlank(message = "Description is blank")
    private String description;

    @NotBlank(message = "title is blank")
    private String title;

    @Schema(name = "image for card, should be in base64",maxLength = 500000)
    @NotBlank(message = "image cant be blank")
    private String image;


    @NotBlank(message = "url is blank")
    private String url;

    @NotBlank(message = "category is blank")
    private String category;

    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "publication is blank")
    private LocalDate publication;

    @NotNull(message = "location is null")
    @Valid
    private LocationFindRequest location;
}

