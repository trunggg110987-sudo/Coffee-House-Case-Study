package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.Recipe;
import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    List<Recipe> getRecipesByProductId(Long productId);
    Recipe saveRecipe(Recipe recipe);
    void deleteRecipe(Long id);
    void addOrUpdateRecipeItem(Long productId, Long ingredientId, Double quantity);
}
