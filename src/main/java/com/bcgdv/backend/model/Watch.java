package com.bcgdv.backend.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Watch {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Watch name is required.")
  @Basic(optional = false)
  private String name;

  @NotNull(message = "Watch price is required.")
  @Basic(optional = false)
  private Double price;

  @Embedded
  private Discount discount;

  public Watch(Long id, @NotNull(message = "Name is required.") String name, @NotNull(message = "Price is required.") Double price, Discount discount) {

  }
 
  public Watch(Long id, @NotNull(message = "Name is required.") String name, @NotNull(message = "Price is required.") Double price) {
    this(id, name, price, null);
  }

  public Watch() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Discount getDiscount() {
    return discount;
  }

  public void setDiscount(Discount discount) {
    this.discount = discount;
  }
}