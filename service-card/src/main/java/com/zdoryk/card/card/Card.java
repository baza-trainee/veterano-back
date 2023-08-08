package com.zdoryk.card.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zdoryk.card.image.Image;
import com.zdoryk.card.location.Location;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@Entity(name = "Card")
public class Card {

    @Id
    @SequenceGenerator(
            name = "card_id_sequence",
            sequenceName = "card_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "card_id_sequence"
    )
    private Long cardId;

    private String description;

    private String title;

    private String url;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @JsonIgnoreProperties("cardList")
    @ManyToOne()
    @JoinColumn(
            name = "location_id",
            nullable = false
    )
    private Location location;

    @JsonIgnoreProperties("card")
    @OneToMany(
            mappedBy = "card",
            fetch = FetchType.EAGER
    )
    private List<Image> imageList;
}
