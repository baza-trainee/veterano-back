package com.zdoryk.card.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CardSaveRequest {

    @NotBlank(message = "Description is blank")
    private String Description;

    @NotBlank(message = "title is blank")
    private String title;

    @NotBlank
    private String image;

    @NotBlank(message = "url is blank")
    private String url;

//    @NotBlank(message = "category is blank")
    private String category;

    private LocationFindRequest location;

}
