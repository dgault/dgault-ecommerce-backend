package com.bcgdv.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="watchOrders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonFormat(pattern = "dd/MM/yyyy") private LocalDate dateCreated;

  private String status;

  @OneToMany(mappedBy = "pk.order")
  @Valid
  private List<WatchOrder> watchOrders = new ArrayList<>();

  @Transient
  public Double getTotalOrderPrice() {
    double sum = 0D;
    List<WatchOrder> watchOrders = getWatchOrders();
    if (watchOrders == null) return sum;
    for (WatchOrder op : watchOrders) {
      sum += op.getTotalPrice();
    }
    if (sum < 0) throw new IllegalArgumentException("Total price cannot be negative");
    return sum;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(LocalDate dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<WatchOrder> getWatchOrders() {
    return watchOrders;
  }

  public void setWatchOrders(List<WatchOrder> watchOrders) {
    this.watchOrders = watchOrders;
  }

  @Transient
  public int getNumberOfWatcheOrders() {
    if (watchOrders == null) return 0;
    return this.watchOrders.size();
  }
}