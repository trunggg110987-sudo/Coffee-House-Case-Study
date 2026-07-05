package com.coffeeshopmanagement.controller;

import com.coffeeshopmanagement.entity.Order;
import com.coffeeshopmanagement.security.CustomUserDetails;
import com.coffeeshopmanagement.service.OrderService;
import com.coffeeshopmanagement.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;

    public OrderController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping("/create/{tableId}")
    public String createOrder(@PathVariable Long tableId, Model model, Authentication authentication) {
        Order order;
        try {
            order = orderService.getOpenOrderByTable(tableId);
        } catch (Exception e) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            order = orderService.createOrder(tableId, userDetails.getAccount().getId());
        }

        model.addAttribute("order", order);
        model.addAttribute("products", productService.findAll());
        return "order/detail";
    }

    @PostMapping("/add-item")
    public String addItemToOrder(@RequestParam Long orderId, @RequestParam Long productId, @RequestParam Integer quantity) {
        orderService.addItemToOrder(orderId, productId, quantity);
        return "redirect:/orders/" + orderId;
    }

    @PostMapping("/update-item")
    public String updateItemQuantity(@RequestParam Long orderDetailId, @RequestParam Integer quantity) {
        Long orderId = orderService.updateItemQuantity(orderDetailId, quantity);
        return "redirect:/orders/" + orderId;
    }

    @PostMapping("/remove-item/{orderDetailId}")
    public String removeItemFromOrder(@PathVariable Long orderDetailId) {
        Long orderId = orderService.removeItemFromOrder(orderDetailId);
        return "redirect:/orders/" + orderId;
    }

    @GetMapping("/list")
    public String listOpenOrders(Model model) {
        List<Order> orders = orderService.getOpenOrders();
        model.addAttribute("orders", orders);
        return "order/list";
    }

    @GetMapping("/{orderId}")
    public String getOrderDetail(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        model.addAttribute("products", productService.findAll());
        return "order/detail";
    }
}
