package com.coffeeshopmanagement.repository;

import com.coffeeshopmanagement.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByDiningTableIdAndStatus(Long tableId, String status);
    List<Order> findByStatus(String status);
}
