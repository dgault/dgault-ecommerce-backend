package com.bcgdv.backend.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bcgdv.backend.dto.WatchOrderDto;
import com.bcgdv.backend.exception.ResourceNotFoundException;
import com.bcgdv.backend.model.Order;
import com.bcgdv.backend.model.WatchOrder;
import com.bcgdv.backend.model.OrderStatus;
import com.bcgdv.backend.service.WatchOrderService;
import com.bcgdv.backend.service.OrderService;
import com.bcgdv.backend.service.WatchService;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  WatchService watchService;
  OrderService orderService;
  WatchOrderService watchOrderService;

  public OrderController(WatchService watchService, OrderService orderService, WatchOrderService watchOrderService) {
    this.watchService = watchService;
    this.orderService = orderService;
    this.watchOrderService = watchOrderService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public @NotNull Iterable<Order> list() {
    Iterable<Order> orders = this.orderService.getAllOrders();
    return orders;
  }

  @PostMapping
  public ResponseEntity<Order> create(@RequestBody WatchOrderForm form) {
    List<WatchOrderDto> formDtos = form.getWatchOrders();
    validateWatchesExistence(formDtos);
    Order order = new Order();
    order.setStatus(OrderStatus.CREATED.name());
    order = this.orderService.create(order);
    List<WatchOrder> watchOrders = new ArrayList<>();
    for (WatchOrderDto dto : formDtos) {
      watchOrders.add(watchOrderService.create(new WatchOrder(order, watchService.getWatch(dto
        .getWatch()
        .getId()), dto.getQuantity())));
    }
    order.setWatchOrders(watchOrders);
    this.orderService.update(order);
    String uri = ServletUriComponentsBuilder
      .fromCurrentServletMapping()
      .path("/api/orders/{id}")
      .buildAndExpand(order.getId())
      .toString();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", uri);

    return new ResponseEntity<>(order, headers, HttpStatus.CREATED);
  }

  @PostMapping(value = "/checkout")
  public ResponseEntity<Map<String, Double>> checkout(@RequestBody List<Long> watchIds) {
    HashMap<String, Double> returnMap = new HashMap<String, Double>();
    validateWatchIDsExistence(watchIds);
    Order order = new Order();
    order.setStatus(OrderStatus.COMPLETE.name());

    List<WatchOrder> watchOrders = new ArrayList<>();
    HashMap<Long, Integer> watchQtys = new HashMap<Long, Integer>();
    for (Long watchID : watchIds) {
      Integer currentQty = watchQtys.getOrDefault(watchID, new Integer(0));
      watchQtys.put(watchID, new Integer(currentQty.intValue() + 1));
    }
    for (Long watchID: watchQtys.keySet()) {
      watchOrders.add(new WatchOrder(order, watchService.getWatch(watchID), watchQtys.get(watchID)));
    }
    order.setWatchOrders(watchOrders);
    returnMap.put("price", order.getTotalOrderPrice());
    HttpHeaders headers = new HttpHeaders();

    return new ResponseEntity<>(returnMap, headers, HttpStatus.OK);
  }

  private void validateWatchesExistence(List<WatchOrderDto> orderWatches) {
    List<WatchOrderDto> list = orderWatches
      .stream()
      .filter(op -> Objects.isNull(watchService.getWatch(op
        .getWatch()
        .getId())))
        .collect(Collectors.toList());

    if (!CollectionUtils.isEmpty(list)) {
      new ResourceNotFoundException("Watch not found");
    }
  }

  private void validateWatchIDsExistence(List<Long> watchIds) {
    List<Long> list = watchIds
      .stream()
      .filter(id -> Objects.isNull(watchService.getWatch(id)))
      .collect(Collectors.toList());

    if (!CollectionUtils.isEmpty(list)) {
      new ResourceNotFoundException("Watch not found");
    }
  }

  public static class WatchOrderForm {
    private List<WatchOrderDto> watchOrders;

    public List<WatchOrderDto> getWatchOrders() {
      return watchOrders;
    }

    public void setWatchOrders(List<WatchOrderDto> watchOrders) {
      this.watchOrders = watchOrders;
    }
  }
}