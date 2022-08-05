package com.bcgdv.backend.dto;

import com.bcgdv.backend.model.Watch;

public class WatchOrderDto {
  private Watch watch;
  private Integer quantity;

  public Watch getWatch() {
    return watch;
  }

  public void setWatch(Watch watch) {
    this.watch = watch;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }
}