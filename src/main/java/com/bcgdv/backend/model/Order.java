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

  @Valid
  private List<WatchOrder> watchOrders = new ArrayList<>();

  @Transient
  public Double getTotalOrderPrice() {
    return 0D;
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
    return this.watchOrders.size();
  }
}