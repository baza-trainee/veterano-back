package com.zdoryk.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class CardDTO {

    private String description;

    private String title;

    private String url;

    private String category;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate publication;

    private Boolean isEnabled;

    private LocationDTO locationDTO;

    private Long imageId;

}
