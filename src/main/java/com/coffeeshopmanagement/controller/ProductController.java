package com.coffeeshopmanagement.controller;
import com.coffeeshopmanagement.entity.Product;
import com.coffeeshopmanagement.service.CategoryService;
import com.coffeeshopmanagement.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "product/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product,
                   RedirectAttributes redirectAttributes) {

    productService.save(product);

    redirectAttributes.addFlashAttribute(
            "successMessage",
            product.getId() == null ?
                    "Product created successfully." :
                    "Product updated successfully."
    );

    return "redirect:/products";
}

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "product/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                     RedirectAttributes redirectAttributes) {

    productService.deleteById(id);

    redirectAttributes.addFlashAttribute(
            "successMessage",
            "Product deleted successfully."
    );

    return "redirect:/products";
}
}
