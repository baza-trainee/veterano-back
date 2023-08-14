package com.zdoryk.card.category;

import com.zdoryk.card.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public Optional<Category> findCategoryByName(String name){
        return categoryRepository.findByCategoryName(name);
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public List<CategoryDto> getAllCategories(){
        return categoryRepository
                .findAll()
                .stream()
                .map(category -> new CategoryDto(
                        category.getCategoryName()
                ))
                .collect(Collectors.toList());
    }
}
