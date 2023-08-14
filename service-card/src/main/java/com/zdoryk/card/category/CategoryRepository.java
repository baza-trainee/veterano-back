package com.zdoryk.card.category;

import com.zdoryk.card.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByCategoryName(String name);

    List<Category> findCategoriesByCategoryName(String name);

}
