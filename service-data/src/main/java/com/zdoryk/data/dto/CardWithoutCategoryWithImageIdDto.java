package com.zdoryk.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CardWithoutCategoryWithImageIdDto {

    private String description;

    private String title;

    private String url;

    private Long imageId;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate publication;

    private LocationDTO location;

}
