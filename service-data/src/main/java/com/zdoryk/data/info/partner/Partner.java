package com.zdoryk.data.info.partner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zdoryk.data.image.Image;
import com.zdoryk.data.url.Url;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "partners")
public class Partner {


    @Id
    @SequenceGenerator(
            name = "partner_id_sequence",
            sequenceName = "partner_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "partner_id_sequence"
    )
    private Long partnerId;

    private String partnerName;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    private String url;

    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate publication;

    private Boolean isEnabled;

    private Boolean isActive;
}
