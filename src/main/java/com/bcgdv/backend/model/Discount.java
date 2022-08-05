package com.bcgdv.backend.model;

import javax.persistence.Embeddable;

@Embeddable
public class Discount {
  
  public enum DiscountType {
    NONE,
    MULTI_BUY;
  }

  private String displayMessage = null;
  private DiscountType type;
  private Integer multiBuyQty = null;
  private Double multiBuyPrice = null;

  public Discount() {
    this.type = DiscountType.NONE;
  }

  public Discount(String displayMessage) {
    this.displayMessage = displayMessage;
    this.type = DiscountType.NONE;
  }
  
  public Discount(DiscountType type, String displayMessage, Integer multiBuyQty, Double multiBuyPrice) {
    this.displayMessage = displayMessage;
    this.type = type;
    this.multiBuyQty = multiBuyQty;
    this.multiBuyPrice = multiBuyPrice;
  }

  public String getDisplayMessage() {
    return displayMessage;
  }
  
  public DiscountType getType() {
    return type;
  }
  
  public Double getMultiBuyPrice() {
    return multiBuyPrice;
  }
  
  public Integer getMultiBuyQty() {
    return multiBuyQty;
  }
}
