package com.zdoryk.data.card;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zdoryk.data.category.Category;
import com.zdoryk.data.image.Image;
import com.zdoryk.data.location.Location;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
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
    @Column(name = "card_id")
    private Long cardId;

    private String description;

    private String title;

    private String url;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate publication;

    private Boolean isEnabled;

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

    @JsonIgnoreProperties("data")
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
