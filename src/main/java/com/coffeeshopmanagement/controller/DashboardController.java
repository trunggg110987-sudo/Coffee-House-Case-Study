package com.coffeeshopmanagement.controller;

import com.coffeeshopmanagement.entity.DiningTable;
import com.coffeeshopmanagement.entity.Order;
import com.coffeeshopmanagement.entity.TableStatus;
import com.coffeeshopmanagement.repository.OrderRepository;
import com.coffeeshopmanagement.service.DiningTableService;
import com.coffeeshopmanagement.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final OrderRepository orderRepository;
    private final DiningTableService diningTableService;
    private final IngredientService ingredientService;

    @GetMapping
    public String index(Model model) {
        List<Order> paidOrders = orderRepository.findByStatus("PAID");
        
        // 1. Total revenue
        BigDecimal totalRevenue = paidOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Today's revenue
        LocalDate today = LocalDate.now();
        BigDecimal todayRevenue = paidOrders.stream()
                .filter(o -> o.getCreatedAt() != null && o.getCreatedAt().toLocalDate().isEqual(today))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Occupied tables count
        long occupiedTablesCount = diningTableService.getAllTables().stream()
                .filter(t -> t.getStatus() == TableStatus.OCCUPIED)
                .count();

        // 4. Low stock ingredients count
        int lowStockCount = ingredientService.getLowStockIngredients().size();

        // 5. Daily revenue for the last 7 days
        Map<String, BigDecimal> dailyRevenueMap = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        
        // Initialize last 7 days with zero
        for (int i = 6; i >= 0; i--) {
            dailyRevenueMap.put(today.minusDays(i).format(formatter), BigDecimal.ZERO);
        }

        // Populate from database
        for (Order o : paidOrders) {
            if (o.getCreatedAt() != null) {
                String dateStr = o.getCreatedAt().toLocalDate().format(formatter);
                if (dailyRevenueMap.containsKey(dateStr)) {
                    BigDecimal current = dailyRevenueMap.get(dateStr);
                    dailyRevenueMap.put(dateStr, current.add(o.getTotalAmount()));
                }
            }
        }

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("todayRevenue", todayRevenue);
        model.addAttribute("occupiedTablesCount", occupiedTablesCount);
        model.addAttribute("lowStockCount", lowStockCount);
        model.addAttribute("dailyRevenue", dailyRevenueMap);
        model.addAttribute("lowStockIngredients", ingredientService.getLowStockIngredients());
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("title", "Dashboard Doanh thu");
        
        return "dashboard/index";
    }
}
