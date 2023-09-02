package com.zdoryk.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto implements Serializable {

    @NotBlank(message = "should exist")
    private String categoryName;
}
