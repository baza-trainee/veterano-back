package com.zdoryk.card.card;

import com.zdoryk.card.category.Category;
import com.zdoryk.card.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {

    List<Card> findByCategoryAndLocation(Category category, Location location);
}
