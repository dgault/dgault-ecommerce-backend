package com.bcgdv.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "order")
public class WatchOrderPK implements Serializable {

  private static final long serialVersionUID = 36293518486154435L;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "watch_id")
  private Watch watch;

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Watch getWatch() {
    return watch;
  }

  public void setWatch(Watch watch) {
    this.watch = watch;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((order.getId() == null)
      ? 0
      : order
        .getId()
        .hashCode());
    result = prime * result + ((watch.getId() == null)
      ? 0
      : watch
        .getId()
        .hashCode());

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
    WatchOrderPK other = (WatchOrderPK) obj;
    if (order == null) {
      if (other.order != null) {
        return false;
      }
    } else if (!order.equals(other.order)) {
      return false;
    }

    if (watch == null) {
      if (other.watch != null) {
        return false;
      }
    } else if (!watch.equals(other.watch)) {
      return false;
    }

    return true;
  }
}
