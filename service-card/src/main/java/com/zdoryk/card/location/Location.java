package com.zdoryk.card.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zdoryk.card.card.Card;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "Location")
public class Location {

    @Id
    @SequenceGenerator(
            name = "location_id_sequence",
            sequenceName = "location_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "location_id_sequence"
    )
    @JsonIgnore
    private Long locationId;

    @Column(
            unique = true,
            nullable = false
    )
    private String country;

    @Column(
            unique = true,
            nullable = false
    )
    private String city;

    @JsonIgnoreProperties({"location","imageList","cardList"})
    @OneToMany(
            mappedBy = "location",
            fetch = FetchType.EAGER
    )
    private List<Card> cardList;

    public Location(String country, String city) {
        this.country = country;
        this.city = city;
    }


    @Override
    public String toString() {
        return "Location{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
