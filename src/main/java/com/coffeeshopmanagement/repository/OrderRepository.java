package com.coffeeshopmanagement.repository;

import com.coffeeshopmanagement.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
