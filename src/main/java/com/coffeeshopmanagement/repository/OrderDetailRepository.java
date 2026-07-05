package com.coffeeshopmanagement.repository;

import com.coffeeshopmanagement.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    OrderDetail findByOrderIdAndProductId(Long orderId, Long productId);
    List<OrderDetail> findByOrderId(Long orderId);
}
