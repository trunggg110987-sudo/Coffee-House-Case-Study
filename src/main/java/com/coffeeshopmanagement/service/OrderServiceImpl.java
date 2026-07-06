package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.*;
import com.coffeeshopmanagement.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DiningTableRepository diningTableRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public OrderServiceImpl(ProductRepository productRepository, OrderRepository orderRepository,
                            DiningTableRepository diningTableRepository, AccountRepository accountRepository,
                            OrderDetailRepository orderDetailRepository, RecipeService recipeService,
                            IngredientService ingredientService) {
        this.diningTableRepository = diningTableRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @Override
    @Transactional
    public Order createOrder(Long tableId, Long accountId) {
        DiningTable diningTable = diningTableRepository.findById(tableId).orElse(null);

        if (diningTable == null) {
            throw new EntityNotFoundException("DiningTable not found");
        }

        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            throw new EntityNotFoundException("Account not found");
        }

        if (orderRepository.findByDiningTableIdAndStatus(tableId, "OPEN") != null) {
            throw new IllegalStateException("Order already exists");
        }
        Order order = new Order();
        order.setAccount(account);
        order.setDiningTable(diningTable);
        order.setStatus("OPEN");
        order.setTotalAmount(BigDecimal.ZERO);
        order.setCreatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void addItemToOrder(Long orderId, Long productId, Integer quantity) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new EntityNotFoundException("Order not found");
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new EntityNotFoundException("Product not found");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        // --- CHECK INGREDIENTS SUFFICIENT ---
        List<Recipe> recipes = recipeService.getRecipesByProductId(productId);
        for (Recipe recipe : recipes) {
            double requiredQuantity = recipe.getQuantity() * quantity;
            double availableQuantity = recipe.getIngredient().getQuantity();

            if (availableQuantity < requiredQuantity) {
                throw new IllegalStateException("Nguyên liệu \"" + recipe.getIngredient().getName()
                        + "\" không đủ để làm món \"" + product.getName() + "\"");
            }
        }

        // --- DEDUCT INGREDIENTS ---
        for (Recipe recipe : recipes) {
            double quantityToDeduct = recipe.getQuantity() * quantity;
            ingredientService.deductStock(recipe.getIngredient().getId(), quantityToDeduct);
        }

        OrderDetail detail = orderDetailRepository.findByOrderIdAndProductId(orderId, productId);
        if (detail == null) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setPriceAtOrder(BigDecimal.valueOf(product.getPrice()));
            orderDetailRepository.save(orderDetail);
        } else {
            detail.setQuantity(detail.getQuantity() + quantity);
            orderDetailRepository.save(detail);
        }
        order.setTotalAmount(calculateOrderTotal(orderId));
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public Long updateItemQuantity(Long orderDetailId, Integer newQuantity) {
        OrderDetail detail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() ->  new EntityNotFoundException("OrderDetail not found"));

        if(newQuantity < 0){
            throw new IllegalArgumentException("Quantity must be 0 or greater");
        }

        int diff = newQuantity - detail.getQuantity();
        if (diff != 0) {
            List<Recipe> recipes = recipeService.getRecipesByProductId(detail.getProduct().getId());
            if (diff > 0) {
                // Check stock for the extra quantity
                for (Recipe recipe : recipes) {
                    double requiredQuantity = recipe.getQuantity() * diff;
                    double availableQuantity = recipe.getIngredient().getQuantity();
                    if (availableQuantity < requiredQuantity) {
                        throw new IllegalStateException("Nguyên liệu \"" + recipe.getIngredient().getName()
                                + "\" không đủ để bổ sung cho món \"" + detail.getProduct().getName() + "\"");
                    }
                }
                // Deduct extra stock
                for (Recipe recipe : recipes) {
                    double quantityToDeduct = recipe.getQuantity() * diff;
                    ingredientService.deductStock(recipe.getIngredient().getId(), quantityToDeduct);
                }
            } else {
                // diff < 0, refund the difference
                int quantityToRefund = -diff;
                for (Recipe recipe : recipes) {
                    double refundAmount = recipe.getQuantity() * quantityToRefund;
                    ingredientService.restock(recipe.getIngredient().getId(), refundAmount);
                }
            }
        }

        detail.setQuantity(newQuantity);
        orderDetailRepository.save(detail);
        Order order = detail.getOrder();
        order.setTotalAmount(calculateOrderTotal(order.getId()));
        orderRepository.save(order);
        return order.getId();
    }

    @Override
    @Transactional
    public Long removeItemFromOrder(Long orderDetailId) {
        OrderDetail detail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() ->  new EntityNotFoundException("OrderDetail not found"));

        Order order = detail.getOrder();
        Long orderId = order.getId();

        // Refund all ingredients
        List<Recipe> recipes = recipeService.getRecipesByProductId(detail.getProduct().getId());
        for (Recipe recipe : recipes) {
            double refundAmount = recipe.getQuantity() * detail.getQuantity();
            ingredientService.restock(recipe.getIngredient().getId(), refundAmount);
        }

        detail.setQuantity(0);
        orderDetailRepository.save(detail);
        order.setTotalAmount(calculateOrderTotal(orderId));
        orderRepository.save(order);
        return orderId;
    }

//    @Override
//    public Order getOrderById(Long orderId) {
//        return orderRepository.findById(orderId)
//                .orElseThrow(() ->  new EntityNotFoundException("Order not found"));
//    }
        @Override
        public Order getOrderById(Long orderId) {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));
            order.getOrderDetails().isEmpty();
            return order;
        }

    @Override
    public List<Order> getOpenOrders() {
        return orderRepository.findByStatus("OPEN");
    }

    @Override
    @Transactional
    public Order getOpenOrderByTable(Long tableId) {
        diningTableRepository.findById(tableId)
                .orElseThrow(() -> new EntityNotFoundException("DiningTable not found"));

        Order order = orderRepository.findByDiningTableIdAndStatus(tableId, "OPEN");

        if (order == null) {
            throw new EntityNotFoundException("Open order not found");
        }

        return order;
    }

    @Override
    public BigDecimal calculateOrderTotal(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        BigDecimal total = BigDecimal.ZERO;

        for (OrderDetail detail : orderDetailRepository.findByOrderId(orderId)) {

            BigDecimal subTotal = detail.getPriceAtOrder()
                    .multiply(BigDecimal.valueOf(detail.getQuantity()));

            total = total.add(subTotal);
        }

        return total;
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Đơn hàng không tồn tại"));

        order.setStatus(status);
        orderRepository.save(order);
    }
}
