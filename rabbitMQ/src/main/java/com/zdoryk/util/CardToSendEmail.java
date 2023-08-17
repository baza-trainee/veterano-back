package com.zdoryk.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardToSendEmail implements Serializable {

    private String description;

    private String title;

    private String url;

    private String category;

    private String country;

    private String city;

}
