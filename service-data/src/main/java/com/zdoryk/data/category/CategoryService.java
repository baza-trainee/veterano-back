package com.zdoryk.data.category;

import com.zdoryk.data.dto.CategoryDto;
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


    public void saveCategories(List<Category> categories){
        categoryRepository.saveAll(categories);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}
