package com.bcgdv.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcgdv.backend.model.WatchOrder;
import com.bcgdv.backend.repository.WatchOrderRepository;

@Service
@Transactional
public class WatchOrderServiceImpl implements WatchOrderService {

  private WatchOrderRepository watchOrderRepository;

  public WatchOrderServiceImpl(WatchOrderRepository watchOrderRepository) {
    this.watchOrderRepository = watchOrderRepository;
  }

  @Override
  public WatchOrder create(WatchOrder watchOrder) {
    return this.watchOrderRepository.save(watchOrder);
  }
}