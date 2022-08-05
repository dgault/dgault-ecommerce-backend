package com.bcgdv.backend.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.bcgdv.backend.model.Discount.DiscountType;

public class WatchOrderTest {
  
  private static final double DELTA = 1e-15;

  WatchOrder watchOrder;

  @Mock
  WatchOrderPK mockWatchOrderPK;
  
  @Mock
  Watch mockWatch;
  
  @Mock
  Order mockOrder;
  
  @Mock
  Discount mockDiscount;
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    
    watchOrder = new WatchOrder(mockOrder, mockWatch, new Integer(3));
    watchOrder.setPk(mockWatchOrderPK);
    Mockito.when(mockWatchOrderPK.getWatch()).thenReturn(mockWatch);
    Mockito.when(mockWatch.getPrice()).thenReturn(45.50);
    Mockito.when(mockWatch.getDiscount()).thenReturn(mockDiscount);
    Mockito.when(mockDiscount.getType()).thenReturn(DiscountType.NONE);
    Mockito.when(mockDiscount.getMultiBuyQty()).thenReturn(3);
    Mockito.when(mockDiscount.getMultiBuyPrice()).thenReturn(120.0);
  }

  @Test
  public void testConstructor() {
    WatchOrder completeWactchOrder = new WatchOrder(mockOrder, mockWatch, new Integer(4));
    assertEquals(new Integer(4), completeWactchOrder.getQuantity());
    assertEquals(mockWatch, completeWactchOrder.getWatch());
    assertEquals(mockOrder, completeWactchOrder.getPk().getOrder());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_nullOrder() {
    WatchOrder testWactchOrder = new WatchOrder(null, mockWatch, new Integer(1));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_nullWatch() {
    WatchOrder emptyWactchOrder = new WatchOrder(mockOrder, null, new Integer(1));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_nullQuantity() {
    WatchOrder emptyWactchOrder = new WatchOrder(mockOrder, mockWatch, null);
  }
  
  @Test
  public void testGetTotalPrice() {
    watchOrder.setQuantity(3);
    Mockito.when(mockWatch.getDiscount()).thenReturn(null);
    assertEquals(136.50, watchOrder.getTotalPrice(), DELTA);
  }
  
  @Test
  public void testGetTotal_Discount() {
    watchOrder.setQuantity(3);
    Mockito.when(mockDiscount.getType()).thenReturn(DiscountType.MULTI_BUY);
    assertEquals(120.00, watchOrder.getTotalPrice(), DELTA);
    
    watchOrder.setQuantity(5);
    assertEquals(211.00, watchOrder.getTotalPrice(), DELTA);
  }
  
  @Test
  public void testGetTotalDiscount_MultipleDiscounts() {
    watchOrder.setQuantity(9);
    Mockito.when(mockDiscount.getType()).thenReturn(DiscountType.MULTI_BUY);
    assertEquals(360.00, watchOrder.getTotalPrice(), DELTA);
    
    watchOrder.setQuantity(10);
    assertEquals(405.50, watchOrder.getTotalPrice(), DELTA);
  }
  
  @Test
  public void testGetTotalPrice_NullDiscount() {
    watchOrder.setQuantity(2);
    Mockito.when(mockWatch.getDiscount()).thenReturn(null);
    assertEquals(91.00, watchOrder.getTotalPrice(), DELTA);
  }
  
  @Test
  public void testGetTotalPrice_NullWatch() {
    watchOrder.setQuantity(5);
    Mockito.when(mockWatchOrderPK.getWatch()).thenReturn(null);
    assertEquals(0.0, watchOrder.getTotalPrice(), DELTA);
  }
  
  @Test
  public void testGetTotalPrice_NullWatchPrice() {
    watchOrder.setQuantity(5);
    Mockito.when(mockWatch.getPrice()).thenReturn(null);
    assertEquals(0.0, watchOrder.getTotalPrice(), DELTA);
  }
  
  @Test
  public void testGetTotalPrice_ZeroQty() {
    watchOrder.setQuantity(0);
    assertEquals(0.0, watchOrder.getTotalPrice(), DELTA);
  }
  
  @Test
  public void testGetWatch() {
    watchOrder.setPk(mockWatchOrderPK);
    assertEquals(mockWatch, watchOrder.getWatch());
    Mockito.when(mockWatchOrderPK.getWatch()).thenReturn(null);
    assertEquals(null, watchOrder.getWatch());
  }
  
  @Test
  public void testGetAndSetQuantity() {
    assertEquals(new Integer(3), watchOrder.getQuantity());
    watchOrder.setQuantity(new Integer(0));
    assertEquals(new Integer(0), watchOrder.getQuantity());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetQuantity_Null() {
    watchOrder.setQuantity(null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSetQuantity_Negative() {
    watchOrder.setQuantity(-10);
  }
  
  @Test
  public void testEquals() {
    WatchOrder comparisonWatchOrder = new WatchOrder(mockOrder, mockWatch, new Integer(3));
    assertFalse(watchOrder.equals(null));
    assertFalse(watchOrder.equals(comparisonWatchOrder));
    assertFalse(watchOrder.equals(new Integer(2)));
    watchOrder.setPk(null);
    watchOrder.setQuantity(new Integer(3));
    assertFalse(watchOrder.equals(comparisonWatchOrder));
    watchOrder.setPk(mockWatchOrderPK);
    watchOrder.setQuantity(new Integer(4));
    comparisonWatchOrder.setPk(mockWatchOrderPK);
    assertFalse(watchOrder.equals(comparisonWatchOrder));
    watchOrder.setQuantity(new Integer(3));
    assertFalse(watchOrder.equals(comparisonWatchOrder));
  }

}
