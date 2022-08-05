package com.bcgdv.backend.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class OrderTest {
  
  private static final double DELTA = 1e-15;
  
  Order order;
  List<WatchOrder> watchList;
  
  @Mock
  WatchOrder watchOrder1;
  
  @Mock
  WatchOrder watchOrder2;
  
  @Mock
  WatchOrder watchOrder3;

  @Before
  public void setup() {
    order = new Order();
    watchList = new ArrayList<WatchOrder>();
    MockitoAnnotations.initMocks(this);
    watchList.add(watchOrder1);
    watchList.add(watchOrder2);
    watchList.add(watchOrder3);
    Mockito.when(watchOrder1.getTotalPrice()).thenReturn(100.50);
    Mockito.when(watchOrder2.getTotalPrice()).thenReturn(50.00);
    Mockito.when(watchOrder3.getTotalPrice()).thenReturn(25.25);
  }

  @Test
  public void testGetTotalOrderPrice() {
    assertEquals(0, order.getTotalOrderPrice().doubleValue(), DELTA);
    order.setWatchOrders(watchList);
    assertEquals(175.75, order.getTotalOrderPrice().doubleValue(), DELTA);
  }
  
  @Test
  public void testGetTotalOrderPrice_Null() {
    order.setWatchOrders(null);
    assertEquals(0, order.getTotalOrderPrice().doubleValue(), DELTA);
  }
  
  @Test
  public void testGetTotalOrderPrice_Overflow() {
    Mockito.when(watchOrder1.getTotalPrice()).thenReturn(Double.MAX_VALUE);
    order.setWatchOrders(watchList);
    assertEquals(Double.MAX_VALUE, order.getTotalOrderPrice().doubleValue(), DELTA);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTotalOrderPrice_Negative() {
    Mockito.when(watchOrder1.getTotalPrice()).thenReturn(-100.00);
    order.setWatchOrders(watchList);
    order.getTotalOrderPrice().doubleValue();
  }

  @Test
  public void 
  testGetNumberOfWatcheOrders()  {
    assertEquals(0, order.getNumberOfWatcheOrders());
    order.setWatchOrders(watchList);
    assertEquals(3, order.getNumberOfWatcheOrders());
  }
  
  @Test
  public void 
  testGetNumberOfWatcheOrders_Null()  {
    order.setWatchOrders(null);
    assertEquals(0, order.getNumberOfWatcheOrders());
  }

}