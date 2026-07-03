package com.coffeeshopmanagement.controller;
import com.coffeeshopmanagement.entity.Category;
import com.coffeeshopmanagement.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "category/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/form";
    }

   @PostMapping("/save")
    public String save(@ModelAttribute Category category,
                   RedirectAttributes redirectAttributes) {

    categoryService.save(category);

    redirectAttributes.addFlashAttribute(
            "successMessage",
            category.getId() == null ?
                    "Category created successfully." :
                    "Category updated successfully."
    );

    return "redirect:/categories";
}

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return "category/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                      RedirectAttributes redirectAttributes) {

    categoryService.deleteById(id);

    redirectAttributes.addFlashAttribute(
            "successMessage",
            "Category deleted successfully."
    );

    return "redirect:/categories";
}
}
