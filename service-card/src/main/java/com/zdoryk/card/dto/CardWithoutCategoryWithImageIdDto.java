package com.zdoryk.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardWithoutCategoryWithImageIdDto {

    private String description;

    private String title;

    private String url;

    private Long imageId;

    private LocationDTO location;

}
