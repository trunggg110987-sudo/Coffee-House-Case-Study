package com.coffeeshopmanagement.service;

import com.coffeeshopmanagement.entity.Order;
import com.coffeeshopmanagement.entity.TableStatus;
import com.coffeeshopmanagement.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final DiningTableService diningTableService;
    private final OrderService orderService;

    public PaymentServiceImpl(OrderRepository orderRepository,
                              DiningTableService diningTableService, OrderService orderService) {
        this.orderRepository = orderRepository;
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

        orderService.updateOrderStatus(orderId, "PAID");
        diningTableService.updateTableStatus(order.getDiningTable().getId(), TableStatus.AVAILABLE.name());
    }
}
