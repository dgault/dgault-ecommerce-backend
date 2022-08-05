package com.bcgdv.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcgdv.backend.model.WatchOrder;
import com.bcgdv.backend.model.WatchOrderPK;

public interface WatchOrderRepository extends JpaRepository<WatchOrder, WatchOrderPK> {
}