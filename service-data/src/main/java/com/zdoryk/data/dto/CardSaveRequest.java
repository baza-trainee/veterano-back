package com.zdoryk.data.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardSaveRequest implements Serializable {

    @Schema(name = "description",example = "text for card")
    @NotBlank(message = "Description is blank")
    private String description;

    @NotBlank(message = "title is blank")
    private String title;

    @Schema(description = "should be in base64",maxLength = 500000)
    @NotBlank(message = "image cant be blank")
    private String image;

    @NotBlank(message = "url is blank")
    private String url;

    @NotNull(message = "categories are null")
    @Valid
    private List<CategoryDto> categories;

    @JsonFormat(pattern="dd.MM.yyyy")
    @NotNull(message = "publication is blank")
    private LocalDate publication;

    @NotNull(message = "location is null")
    @Valid
    private LocationDTO location;

}

