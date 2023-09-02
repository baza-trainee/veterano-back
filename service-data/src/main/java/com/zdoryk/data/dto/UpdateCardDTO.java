package com.zdoryk.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCardDTO implements Serializable {

    private Long cardId;

    private String description;

    private String title;

    private String url;

    private String image;

    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate publication;

    @Valid
    private List<CategoryDto> categories;

    private LocationDTO location;

    private Boolean isEnabled;
}
