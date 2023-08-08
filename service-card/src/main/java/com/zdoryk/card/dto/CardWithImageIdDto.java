package com.zdoryk.card.dto;

import com.zdoryk.card.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardWithImageIdDto {

    private String description;

    private String title;

    private String url;

    private Long imageId;

    private LocationDTO location;
}
