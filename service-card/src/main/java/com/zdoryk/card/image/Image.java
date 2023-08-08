package com.zdoryk.card.image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zdoryk.card.card.Card;
import com.zdoryk.card.location.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Entity(name = "image")
public class Image {

    @Id
    @SequenceGenerator(
            name = "image_id_sequence",
            sequenceName = "image_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_id_sequence"
    )
    private Long imageId;

    @Column(length= 500000)
    private String image;


    @JsonIgnoreProperties({"imageList","card"})
    @ManyToOne()
    @JoinColumn(
            name = "card_id",
            nullable = false
    )
    private Card card;

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", image='" + image + '\'' +
                '}';
    }
}
