package com.coffeeshopmanagement.controller;

import com.coffeeshopmanagement.entity.Product;
import com.coffeeshopmanagement.entity.Recipe;
import com.coffeeshopmanagement.repository.ProductRepository;
import com.coffeeshopmanagement.service.IngredientService;
import com.coffeeshopmanagement.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final ProductRepository productRepository;
    private final IngredientService ingredientService;

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("activePage", "recipes");
        model.addAttribute("title", "Quản lý Công thức");
        return "recipe/list";
    }

    @GetMapping("/product/{productId}")
    public String showProductRecipe(@PathVariable Long productId, Model model) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm: " + productId));
        
        List<Recipe> recipes = recipeService.getRecipesByProductId(productId);
        
        model.addAttribute("product", product);
        model.addAttribute("recipes", recipes);
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        model.addAttribute("activePage", "recipes");
        model.addAttribute("title", "Công thức - " + product.getName());
        return "recipe/form";
    }

    @PostMapping("/product/{productId}/add")
    public String addOrUpdateRecipeItem(@PathVariable Long productId,
                                        @RequestParam Long ingredientId,
                                        @RequestParam Double quantity) {
        recipeService.addOrUpdateRecipeItem(productId, ingredientId, quantity);
        return "redirect:/recipes/product/" + productId;
    }

    @GetMapping("/delete/{id}")
    public String deleteRecipeItem(@PathVariable Long id, @RequestParam Long productId) {
        recipeService.deleteRecipe(id);
        return "redirect:/recipes/product/" + productId;
    }
}
