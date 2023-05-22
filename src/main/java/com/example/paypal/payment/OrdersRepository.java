package com.example.paypal.payment;

import com.example.paypal.payment.model.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findByOrderId(String orderId);
}
