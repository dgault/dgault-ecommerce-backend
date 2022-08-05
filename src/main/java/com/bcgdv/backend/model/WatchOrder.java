package com.bcgdv.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class WatchOrder {

  @EmbeddedId
  @JsonIgnore
  private WatchOrderPK pk;

  @Column(nullable = false) private Integer quantity;

  public WatchOrder() {
    super();
  }

  public WatchOrder(Order order, Watch watch, Integer quantity) {

  }

  @Transient
  public Watch getWatch() {
    return null;
  }

  @Transient
  public Double getTotalPrice() {
    return 0D;
  }

  public WatchOrderPK getPk() {
    return pk;
  }

  public void setPk(WatchOrderPK pk) {
    this.pk = pk;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

}