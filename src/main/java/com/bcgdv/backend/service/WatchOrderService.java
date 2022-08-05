package com.bcgdv.backend.service;

import org.springframework.validation.annotation.Validated;

import com.bcgdv.backend.model.WatchOrder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface WatchOrderService {

  WatchOrder create(@NotNull(message = "The order cannot have null watches") @Valid WatchOrder watchOrder);
}