package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.Ingredient;
import java.util.List;

public interface IngredientService {
    List<Ingredient> getAllIngredients();
    Ingredient getIngredientById(Long id);
    Ingredient saveIngredient(Ingredient ingredient);
    void deleteIngredient(Long id);
    List<Ingredient> getLowStockIngredients();
}
