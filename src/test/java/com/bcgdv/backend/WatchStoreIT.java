package com.bcgdv.backend;

import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bcgdv.backend.controller.OrderController;
import com.bcgdv.backend.controller.WatchController;
import com.bcgdv.backend.dto.WatchOrderDto;
import com.bcgdv.backend.model.Order;
import com.bcgdv.backend.model.Watch;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import org.junit.runners.MethodSorters;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { WatchStore.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WatchStoreIT {

  private static final double DELTA = 1e-15;

  @Autowired private TestRestTemplate restTemplate;

  @LocalServerPort private int port;

  @Autowired private WatchController productController;

  @Autowired private OrderController orderController;
  
  @Test
  public void contextLoads() {
    Assertions
      .assertThat(productController)
      .isNotNull();
    Assertions
      .assertThat(orderController)
      .isNotNull();
  }

  @Test
  public void testGetWatches() {
    ResponseEntity<Iterable<Watch>> responseEntity = 
      restTemplate.exchange("http://localhost:" + port + "/api/watches", HttpMethod.GET, null, new ParameterizedTypeReference<Iterable<Watch>>() {});
    Iterable<Watch> products = responseEntity.getBody();
    Assertions
      .assertThat(products)
      .hasSize(4);

    assertThat(products, hasItem(hasProperty("name", is("Rolex"))));
    assertThat(products, hasItem(hasProperty("name", is("Michael Kors"))));
    assertThat(products, hasItem(hasProperty("name", is("Swatch"))));
    assertThat(products, hasItem(hasProperty("name", is("Casio"))));
  }

  @Test
  @Transactional
  public void testGetOrder() {
    ResponseEntity<Iterable<Order>> responseEntity = 
      restTemplate.exchange("http://localhost:" + port + "/api/orders", HttpMethod.GET, null, new ParameterizedTypeReference<Iterable<Order>>() {});

    Iterable<Order> orders = responseEntity.getBody();
    Assertions
      .assertThat(orders)
      .hasSize(0);
  }

  @Test
  @Transactional
  public void testOrderCreate() {
    final ResponseEntity<Order> postResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/orders", prepareOrderForm(), Order.class);
    Order order = postResponse.getBody();
    Assertions
      .assertThat(postResponse.getStatusCode())
      .isEqualByComparingTo(HttpStatus.CREATED);

    assertThat(order, hasProperty("status", is("CREATED")));
    assertThat(order.getWatchOrders(), hasItem(hasProperty("quantity", is(2))));
  }
    
  @Test
  @Transactional
  public void testOrderCheckOut() {
    List<Long> watchIDs = Arrays.asList(1L, 2L, 3L);
    HttpEntity<List<Long>> request = new HttpEntity<>(watchIDs);
    final ResponseEntity<Map<String, Double>> postResponse = (ResponseEntity<Map<String, Double>>) 
      restTemplate.exchange("http://localhost:" + port + "/api/orders/checkout", HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Double>>() {});
    Double price = postResponse.getBody().get("price");
    Assertions
      .assertThat(postResponse.getStatusCode())
      .isEqualByComparingTo(HttpStatus.OK);
    assertEquals(230.00, price.doubleValue(), DELTA);
  }
  
  @Test
  @Transactional
  public void testOrderCheckOut_Discount() {
    List<Long> watchIDs = Arrays.asList(2L, 1L, 2L, 4L);
    HttpEntity<List<Long>> request = new HttpEntity<>(watchIDs);
    final ResponseEntity<Map<String, Double>> postResponse = (ResponseEntity<Map<String, Double>>) 
      restTemplate.exchange("http://localhost:" + port + "/api/orders/checkout", HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Double>>() {});
    Double price = postResponse.getBody().get("price");
    Assertions
      .assertThat(postResponse.getStatusCode())
      .isEqualByComparingTo(HttpStatus.OK);
    assertEquals(250.00, price.doubleValue(), DELTA);
  }
  
  @Test
  @Transactional
  public void testOrderCheckOut_MultipleDiscounts() {
    List<Long> watchIDs = Arrays.asList(2L, 2L, 2L, 2L, 1L, 1L, 1L, 4L);
    HttpEntity<List<Long>> request = new HttpEntity<>(watchIDs);
    final ResponseEntity<Map<String, Double>> postResponse = (ResponseEntity<Map<String, Double>>) 
      restTemplate.exchange("http://localhost:" + port + "/api/orders/checkout", HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Double>>() {});
    Double price = postResponse.getBody().get("price");
    Assertions
      .assertThat(postResponse.getStatusCode())
      .isEqualByComparingTo(HttpStatus.OK);
    assertEquals(470.00, price.doubleValue(), DELTA);
  }

  private OrderController.WatchOrderForm prepareOrderForm() {
    OrderController.WatchOrderForm orderForm = new OrderController.WatchOrderForm();
    WatchOrderDto watchDto = new WatchOrderDto();
    watchDto.setWatch(new Watch(3L, "Swatch", 50.00));
    watchDto.setQuantity(2);
    orderForm.setWatchOrders(Collections.singletonList(watchDto));

    return orderForm;
  }
}