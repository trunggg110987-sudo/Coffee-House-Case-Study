package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.Ingredient;
import com.coffeeshopmanagement.repository.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nguyên liệu có ID: " + id));
    }

    @Override
    @Transactional
    public void saveIngredient(Ingredient ingredient) {
        ingredientRepository.save(ingredient);
    }

    @Override
    @Transactional
    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }

    @Override
    public List<Ingredient> getLowStockIngredients() {
        return ingredientRepository.findLowStockIngredients();
    }

    @Override
    @Transactional
    public void deductStock(Long ingredientId, Double quantityToDeduct) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException("Nguyên liệu không tồn tại"));

        if (ingredient.getQuantity() < quantityToDeduct) {
            throw new IllegalStateException("Nguyên liệu \"" + ingredient.getName() + "\" không đủ");
        }

        ingredient.setQuantity(ingredient.getQuantity() - quantityToDeduct);
        ingredientRepository.save(ingredient);
    }
}
