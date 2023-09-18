package com.zdoryk.data.card;

import com.zdoryk.data.category.Category;
import com.zdoryk.data.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {

    List<Card> findCardByIsEnabledFalse();
    List<Card> findCardByIsEnabledTrue();

    List<Card> findCardByIsActiveTrue();
}
