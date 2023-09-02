package com.zdoryk.data.category;

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
@Builder
@Getter
@Setter
@Entity(name = "category")
public class Category {

    @Id
    @SequenceGenerator(
            name = "category_id_sequence",
            sequenceName = "category_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_id_sequence"
    )
    @JsonIgnore
    private Long categoryId;

    @Column(
            nullable = false,
            unique = true
    )
    private String categoryName;

    @JsonIgnoreProperties({"location","imageList","cardList","category"})
    @ManyToMany(
            mappedBy = "categories",
            fetch = FetchType.EAGER
    )
    private List<Card> cardList;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Category category = (Category) object;
        return getCategoryId() != null && Objects.equals(getCategoryId(), category.getCategoryId());
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}


