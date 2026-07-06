package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.Ingredient;
import java.util.List;

public interface IngredientService {
    List<Ingredient> getAllIngredients();
    Ingredient getIngredientById(Long id);
    void saveIngredient(Ingredient ingredient);
    void deleteIngredient(Long id);
    List<Ingredient> getLowStockIngredients();
    void restock(Long id, Double quantity);
    void deductStock(Long ingredientId, Double quantityToDeduct);
}
