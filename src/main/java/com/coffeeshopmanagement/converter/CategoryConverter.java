package com.coffeeshopmanagement.converter;

import com.coffeeshopmanagement.entity.Category;
import com.coffeeshopmanagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<String, Category> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        return categoryRepository.findById(Long.parseLong(source))
                .orElse(null);
    }
}