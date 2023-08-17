package com.zdoryk.data.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zdoryk.data.card.Card;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Location location = (Location) object;
        return getLocationId() != null && Objects.equals(getLocationId(), location.getLocationId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
