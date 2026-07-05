package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.Order;
import java.util.List;
import java.math.BigDecimal;

public interface OrderService {
    Order createOrder(Long tableId, Long accountId);
    void addItemToOrder(Long orderId, Long productId, Integer quantity);
    void updateItemQuantity(Long orderDetailId, Integer newQuantity);
    void removeItemFromOrder(Long orderDetailId);
    Order getOrderById(Long orderId);
    List<Order> getOpenOrders();
    Order getOpenOrderByTable(Long tableId);
    BigDecimal calculateOrderTotal(Long orderId);
    void updateOrderStatus(Long orderId, String paid);
}
