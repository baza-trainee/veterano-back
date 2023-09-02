package com.zdoryk.data.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zdoryk.data.card.Card;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Entity(name = "images")
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


    @JsonIgnoreProperties({"imageList","data"})
    @JsonIgnore
    @OneToOne()
    @JoinColumn(
            name = "card_id"
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
