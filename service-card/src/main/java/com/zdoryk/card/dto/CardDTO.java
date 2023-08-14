package com.zdoryk.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CardDTO {

    private String description;

    private String title;

    private String url;

    private String category;

    private LocationDTO locationDTO;


}
