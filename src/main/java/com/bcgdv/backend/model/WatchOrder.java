package com.bcgdv.backend.model;

import com.bcgdv.backend.model.Discount.DiscountType;
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
    if (order == null || watch == null || quantity == null) 
      throw new IllegalArgumentException("Input paramaters cannot be null");
    pk = new WatchOrderPK();
    pk.setOrder(order);
    pk.setWatch(watch);
    this.quantity = quantity;
  }

  @Transient
  public Watch getWatch() {
    if (pk == null) return null;
    return pk.getWatch();
  }

  @Transient
  public Double getTotalPrice() {
    if (getWatch() == null || getWatch().getPrice() == null || getQuantity() == null) return 0.0;
    Double totalPrice = getWatch().getPrice() * getQuantity();
    
    // If watch has a discount then adjust the total
    Discount discount = getWatch().getDiscount();
    if (discount != null && discount.getType() == DiscountType.MULTI_BUY) {
      int numDiscountsToApply = getQuantity() / discount.getMultiBuyQty();
      if (numDiscountsToApply > 0) {
        totalPrice -= numDiscountsToApply * discount.getMultiBuyQty() * getWatch().getPrice();
        totalPrice += numDiscountsToApply * discount.getMultiBuyPrice();
      }
    }
    return totalPrice;
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
    if (quantity == null || quantity < 0) 
      throw new IllegalArgumentException("Input paramaters cannot be null and must be positive");
    this.quantity = quantity;
  }

  @Override
  public int hashCode() {
    final int prime = 29;
    int result = 1;
    result = prime * result + ((pk == null) ? 0 : pk.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    WatchOrder other = (WatchOrder) obj;
    if (pk == null) {
      if (other.pk != null) {
        return false;
      }
    } else if (!pk.equals(other.pk)) {
      return false;
    }
    if (quantity != other.getQuantity()) {
      return false;
    }
    return true;
  }
}