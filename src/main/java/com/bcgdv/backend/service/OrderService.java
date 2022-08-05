package com.bcgdv.backend.service;

import org.springframework.validation.annotation.Validated;

import com.bcgdv.backend.model.Order;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface OrderService {

  @NotNull Iterable<Order> getAllOrders();

  Order create(@NotNull(message = "The order cannot be null.") @Valid Order order);

  void update(@NotNull(message = "The order cannot be null.") @Valid Order order);
}
