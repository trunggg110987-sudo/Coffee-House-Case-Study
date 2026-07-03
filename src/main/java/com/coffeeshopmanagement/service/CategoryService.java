package com.coffeeshopmanagement.service;
import com.coffeeshopmanagement.entity.Category;
import java.util.List;

public interface CategoryService {
     List<Category> findAll();

    Category findById(Long id);

    Category save(Category category);

    void deleteById(Long id);
}
