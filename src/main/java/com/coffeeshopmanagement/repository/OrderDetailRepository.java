package com.coffeeshopmanagement.repository;

import com.coffeeshopmanagement.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
}
