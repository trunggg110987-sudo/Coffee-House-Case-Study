package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.Order;
import com.coffeeshopmanagement.entity.OrderDetail;
import com.coffeeshopmanagement.entity.Recipe;
import com.coffeeshopmanagement.entity.TableStatus;
import com.coffeeshopmanagement.repository.OrderDetailRepository;
import com.coffeeshopmanagement.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final DiningTableService diningTableService;
    private final OrderService orderService;

    public PaymentServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
                              RecipeService recipeService, IngredientService ingredientService,
                              DiningTableService diningTableService, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.diningTableService = diningTableService;
        this.orderService = orderService;
    }

    @Override
    public void processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Đơn hàng không tồn tại"));

        if (!order.getStatus().equals("OPEN")) {
            throw new IllegalStateException("Chỉ có thể thanh toán đơn hàng đang mở");
        }

        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);

        for (OrderDetail detail : orderDetails) {
            List<Recipe> recipes = recipeService.getRecipesByProductId(detail.getProduct().getId());

            for (Recipe recipe : recipes) {
                double requiredQuantity = recipe.getQuantity() * detail.getQuantity();
                double availableQuantity = recipe.getIngredient().getQuantity();

                if (availableQuantity < requiredQuantity) {
                    throw new IllegalStateException("Nguyên liệu \"" + recipe.getIngredient().getName()
                            + "\" không đủ để hoàn thành đơn hàng");
                }
            }
        }

        for (OrderDetail detail : orderDetails) {
            List<Recipe> recipes = recipeService.getRecipesByProductId(detail.getProduct().getId());

            for (Recipe recipe : recipes) {
                double quantityToDeduct = recipe.getQuantity() * detail.getQuantity();
                ingredientService.deductStock(recipe.getIngredient().getId(), quantityToDeduct);
            }
        }
        orderService.updateOrderStatus(orderId, "PAID");
        diningTableService.updateTableStatus(order.getDiningTable().getId(), TableStatus.AVAILABLE.name());
    }
}
