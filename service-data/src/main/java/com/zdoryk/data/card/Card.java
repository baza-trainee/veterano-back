package com.zdoryk.data.card;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zdoryk.data.category.Category;
import com.zdoryk.data.image.Image;
import com.zdoryk.data.location.Location;
import com.zdoryk.data.url.Url;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "Card")
public class Card implements Serializable {

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

    private String title;

    private String description;

    @JsonIgnoreProperties({"location","imageList","cardList","url"})
    @OneToOne(
            mappedBy = "card",
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE}
    )
    private Url url;

    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate publication;

    private Boolean isEnabled;

    @JsonIgnoreProperties("cardList")
    @ManyToMany()
    @JoinColumn(
            name = "category",
            nullable = false
    )
    private List<Category> categories;

    @JsonIgnoreProperties("cardList")
    @ManyToOne()
    @JoinColumn(
            name = "location_id",
            nullable = false
    )
    private Location location;

    @JsonIgnoreProperties("data")
    @OneToOne(
            mappedBy = "card",
            fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE
    )
    private Image image;

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
