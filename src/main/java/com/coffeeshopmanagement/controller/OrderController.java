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
    private final com.coffeeshopmanagement.repository.AccountRepository accountRepository;
    private final com.coffeeshopmanagement.repository.OrderDetailRepository orderDetailRepository;

    public OrderController(OrderService orderService, ProductService productService,
                           com.coffeeshopmanagement.repository.AccountRepository accountRepository,
                           com.coffeeshopmanagement.repository.OrderDetailRepository orderDetailRepository) {
        this.orderService = orderService;
        this.productService = productService;
        this.accountRepository = accountRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @GetMapping("")
    public String index() {
        return "redirect:/orders/list";
    }

    @GetMapping("/create/{tableId}")
    public String createOrder(@PathVariable Long tableId, Model model, Authentication authentication) {
        Order order;
        try {
            order = orderService.getOpenOrderByTable(tableId);
        } catch (Exception e) {
            Long accountId = null;
            if (authentication != null && authentication.isAuthenticated() 
                    && !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                accountId = userDetails.getAccount().getId();
            } else {
                // Fallback to the first account in the database (admin or staff)
                accountId = accountRepository.findAll().stream()
                        .map(com.coffeeshopmanagement.entity.Account::getId)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No accounts found in database"));
            }
            order = orderService.createOrder(tableId, accountId);
        }

        model.addAttribute("order", order);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("activePage", "orders");
        model.addAttribute("title", "Chi tiết đơn hàng");
        return "order/detail";
    }

    @PostMapping("/add-item")
    public String addItemToOrder(@RequestParam Long orderId, @RequestParam Long productId, @RequestParam Integer quantity,
                                 org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            orderService.addItemToOrder(orderId, productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm món thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/orders/" + orderId;
    }

    @PostMapping("/update-item")
    public String updateItemQuantity(@RequestParam Long orderDetailId, @RequestParam Integer quantity,
                                     org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        Long orderId = orderDetailRepository.findById(orderDetailId)
                .map(d -> d.getOrder().getId())
                .orElse(null);
        try {
            orderService.updateItemQuantity(orderDetailId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật số lượng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return orderId != null ? "redirect:/orders/" + orderId : "redirect:/orders/list";
    }

    @PostMapping("/remove-item/{orderDetailId}")
    public String removeItemFromOrder(@PathVariable Long orderDetailId,
                                      org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        Long orderId = orderDetailRepository.findById(orderDetailId)
                .map(d -> d.getOrder().getId())
                .orElse(null);
        try {
            orderService.removeItemFromOrder(orderDetailId);
            redirectAttributes.addFlashAttribute("successMessage", "Xoá món thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return orderId != null ? "redirect:/orders/" + orderId : "redirect:/orders/list";
    }

    @GetMapping("/list")
    public String listOpenOrders(Model model) {
        List<Order> orders = orderService.getOpenOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("activePage", "orders");
        model.addAttribute("title", "Đơn hàng đang hoạt động");
        return "order/list";
    }

    @GetMapping("/{orderId}")
    public String getOrderDetail(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("activePage", "orders");
        model.addAttribute("title", "Chi tiết đơn hàng");
        return "order/detail";
    }

}
