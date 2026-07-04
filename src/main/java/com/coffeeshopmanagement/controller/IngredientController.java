package com.coffeeshopmanagement.controller;

import com.coffeeshopmanagement.entity.Ingredient;
import com.coffeeshopmanagement.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public String listIngredients(Model model) {
        model.addAttribute("ingredients", ingredientService.getAllIngredients());
        model.addAttribute("lowStockIngredients", ingredientService.getLowStockIngredients());
        model.addAttribute("activePage", "ingredients");
        model.addAttribute("title", "Quản lý Nguyên liệu");
        return "ingredient/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ingredient", new Ingredient());
        model.addAttribute("activePage", "ingredients");
        model.addAttribute("title", "Thêm nguyên liệu");
        return "ingredient/form";
    }

    @PostMapping
    public String saveIngredient(@Valid @ModelAttribute("ingredient") Ingredient ingredient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("activePage", "ingredients");
            model.addAttribute("title", ingredient.getId() == null ? "Thêm nguyên liệu" : "Chỉnh sửa nguyên liệu");
            return "ingredient/form";
        }
        ingredientService.saveIngredient(ingredient);
        return "redirect:/ingredients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("ingredient", ingredientService.getIngredientById(id));
        model.addAttribute("activePage", "ingredients");
        model.addAttribute("title", "Chỉnh sửa nguyên liệu");
        return "ingredient/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return "redirect:/ingredients";
    }
}
