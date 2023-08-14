package com.zdoryk.card.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zdoryk.card.category.Category;
import com.zdoryk.card.image.Image;
import com.zdoryk.card.location.Location;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    @Column(name = "card_id")
    private Long cardId;

    private String description;

    private String title;

    private String url;

    @JsonIgnoreProperties("cardList")
    @ManyToOne()
    @JoinColumn(
            name = "category",
            nullable = false
    )
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

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Card card = (Card) object;
        return getCardId() != null && Objects.equals(getCardId(), card.getCardId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
