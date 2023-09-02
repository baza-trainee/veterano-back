package com.zdoryk.data.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;

public record PartnerDTO(

         Long id,
         @NotBlank(message = "partner's name is blank")
         String partnerName,

         @NotBlank(message = "partner's image is blank")
         String image,

         @NotBlank(message = "partner's url is blank")
         String url,

         @NotNull
         @JsonFormat(pattern="dd.MM.yyyy")
         LocalDate publication,

         @NotNull(message = "is partner with us?")
         Boolean isEnabled


) implements Serializable {}
