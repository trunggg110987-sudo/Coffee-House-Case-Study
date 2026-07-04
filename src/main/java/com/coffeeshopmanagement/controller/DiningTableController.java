package com.coffeeshopmanagement.controller;

import com.coffeeshopmanagement.entity.DiningTable;
import com.coffeeshopmanagement.entity.TableStatus;
import com.coffeeshopmanagement.service.DiningTableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tables")
@RequiredArgsConstructor
public class DiningTableController {

    private final DiningTableService tableService;

    @GetMapping
    public String listTables(Model model) {
        model.addAttribute("tables", tableService.getAllTables());
        model.addAttribute("activePage", "tables");
        model.addAttribute("title", "Quản lý Bàn");
        return "table/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("table", new DiningTable());
        model.addAttribute("statuses", TableStatus.values());
        model.addAttribute("activePage", "tables");
        model.addAttribute("title", "Thêm bàn mới");
        return "table/form";
    }

    @PostMapping
    public String saveTable(@Valid @ModelAttribute("table") DiningTable table, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", TableStatus.values());
            model.addAttribute("activePage", "tables");
            model.addAttribute("title", table.getId() == null ? "Thêm bàn mới" : "Chỉnh sửa bàn");
            return "table/form";
        }
        tableService.saveTable(table);
        return "redirect:/tables";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("table", tableService.getTableById(id));
        model.addAttribute("statuses", TableStatus.values());
        model.addAttribute("activePage", "tables");
        model.addAttribute("title", "Chỉnh sửa bàn");
        return "table/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return "redirect:/tables";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable Long id) {
        tableService.toggleTableStatus(id);
        return "redirect:/tables";
    }
}
