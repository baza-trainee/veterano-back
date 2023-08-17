package com.zdoryk.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardWithOutLocation {

    private String description;

    private String title;

    private String url;

    private String category;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate publication;

    private Long imageId;

}
