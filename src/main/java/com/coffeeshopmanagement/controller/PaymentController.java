package com.coffeeshopmanagement.controller;

import com.coffeeshopmanagement.entity.Order;
import com.coffeeshopmanagement.service.OrderService;
import com.coffeeshopmanagement.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout/{orderId}")
    public String checkoutPage(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "payment/checkout";
    }

    @PostMapping("/process")
    public String processPayment(@RequestParam Long orderId, Model model) {
        try {
            paymentService.processPayment(orderId);
            return "redirect:/payments/success/" + orderId;
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("order", orderService.getOrderById(orderId));
            return "payment/checkout";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("order", orderService.getOrderById(orderId));
            return "payment/checkout";
        }
    }

    @GetMapping("/success/{orderId}")
    public String paymentSuccess(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "payment/success";
    }
}