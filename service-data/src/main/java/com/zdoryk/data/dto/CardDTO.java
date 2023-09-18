package com.zdoryk.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CardDTO(

    Long cardId,
    String description,
    String title,

    UUID urlId,
    String url,
    Long imageId,
    @JsonFormat(pattern="dd.MM.yyyy")
    LocalDate publication,
    List<CategoryDto> categories,
    LocationDTO location,
    Boolean isEnabled
) implements Serializable {}
