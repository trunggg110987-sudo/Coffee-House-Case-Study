package com.coffeeshopmanagement.repository;

import com.coffeeshopmanagement.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByProductId(Long productId);
    Optional<Recipe> findByProductIdAndIngredientId(Long productId, Long ingredientId);
    void deleteByProductId(Long productId);
}
