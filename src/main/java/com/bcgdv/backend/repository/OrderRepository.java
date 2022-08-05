package com.bcgdv.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bcgdv.backend.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}