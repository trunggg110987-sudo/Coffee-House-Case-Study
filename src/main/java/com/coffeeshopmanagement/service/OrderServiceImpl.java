package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.Order;
import com.coffeeshopmanagement.entity.Product;
import com.coffeeshopmanagement.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Long tableId, Long accountId) {
    }

    @Override
    public void addItemToOrder(Long orderId, Long productId, Integer quantity) {
        Order order = orderRepository
    }

    @Override
    public void updateItemQuantity(Long orderDetailId, Integer newQuantity) {
        Order order = orderRepository.getOne(orderDetailId);
    }

    @Override
    public void removeItemFromOrder(Long orderDetailId) {

    }

    @Override
    public Order getOrderById(Long orderId) {
        return null;
    }

    @Override
    public List<Order> getOpenOrders() {
        return List.of();
    }

    @Override
    public Order getOpenOrderByTable(Long tableId) {
        return null;
    }

    @Override
    public BigDecimal calculateOrderTotal(Long orderId) {
        return null;
    }
}
