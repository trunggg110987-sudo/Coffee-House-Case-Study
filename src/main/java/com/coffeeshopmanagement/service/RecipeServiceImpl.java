package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.Ingredient;
import com.coffeeshopmanagement.entity.Product;
import com.coffeeshopmanagement.entity.Recipe;
import com.coffeeshopmanagement.repository.IngredientRepository;
import com.coffeeshopmanagement.repository.ProductRepository;
import com.coffeeshopmanagement.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public List<Recipe> getRecipesByProductId(Long productId) {
        return recipeRepository.findByProductId(productId);
    }

    @Override
    @Transactional
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    @Transactional
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addOrUpdateRecipeItem(Long productId, Long ingredientId, Double quantity) {
        Optional<Recipe> existing = recipeRepository.findByProductIdAndIngredientId(productId, ingredientId);
        if (existing.isPresent()) {
            Recipe recipe = existing.get();
            recipe.setQuantity(quantity);
            recipeRepository.save(recipe);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm có ID: " + productId));
            Ingredient ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nguyên liệu có ID: " + ingredientId));
            Recipe recipe = Recipe.builder()
                    .product(product)
                    .ingredient(ingredient)
                    .quantity(quantity)
                    .build();
            recipeRepository.save(recipe);
        }
    }
}
