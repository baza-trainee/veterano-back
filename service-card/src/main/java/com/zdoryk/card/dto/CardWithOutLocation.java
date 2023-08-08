package com.zdoryk.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardWithOutLocation {

    private String description;

    private String title;

    private String url;

    private Long imageId;

}
