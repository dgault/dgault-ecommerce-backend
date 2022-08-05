package com.bcgdv.backend.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import com.bcgdv.backend.dto.WatchOrderDto;
import com.bcgdv.backend.model.Discount;
import com.bcgdv.backend.model.Order;
import com.bcgdv.backend.model.Watch;
import com.bcgdv.backend.model.WatchOrder;
import com.bcgdv.backend.model.Discount.DiscountType;
import com.bcgdv.backend.service.OrderService;
import com.bcgdv.backend.service.WatchOrderService;
import com.bcgdv.backend.service.WatchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrderController.class)
@RunWith(SpringRunner.class)
public class OrderControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper mapper;
  
  @MockBean
  OrderService orderService;
  
  @MockBean
  WatchService watchService;
  
  @MockBean
  WatchOrderService watchOrderService;
  
  private Order testOrder1 = new Order();
  private Order testOrder2 = new Order();
  private Order testOrder3 = new Order();

  private Discount threeFor200 = new Discount(DiscountType.MULTI_BUY, "3 for 200", new Integer(3), new Double(200.00));
  private Discount twoFor120 = new Discount(DiscountType.MULTI_BUY, "2 for 120", new Integer(2), new Double(120.00));
  
  @Mock
  private Watch mockWatch1;
  
  @Mock
  private Watch mockWatch2;
  
  @Mock
  private Watch mockWatch3;
  
  @Mock
  private Watch mockWatch4;
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    Mockito.when(mockWatch1.getPrice()).thenReturn(100.00);
    Mockito.when(mockWatch2.getPrice()).thenReturn(80.00);
    Mockito.when(mockWatch3.getPrice()).thenReturn(50.00);
    Mockito.when(mockWatch4.getPrice()).thenReturn(30.00);
    Mockito.when(mockWatch1.getDiscount()).thenReturn(threeFor200);
    Mockito.when(mockWatch2.getDiscount()).thenReturn(twoFor120);
    Mockito.when(mockWatch3.getDiscount()).thenReturn(null);
    Mockito.when(mockWatch4.getDiscount()).thenReturn(null);
    Mockito.when(watchService.getWatch(1L)).thenReturn(mockWatch1);
    Mockito.when(watchService.getWatch(2L)).thenReturn(mockWatch2);
    Mockito.when(watchService.getWatch(3L)).thenReturn(mockWatch3);
    Mockito.when(watchService.getWatch(4L)).thenReturn(mockWatch4);
  }

  @Test
  public void testList_Empty() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .get("/api/orders")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }
  
  @Test
  public void testList() throws Exception {
    List<Order> records = new ArrayList<>(Arrays.asList(testOrder1, testOrder2, testOrder3));
    Mockito.when(orderService.getAllOrders()).thenReturn(records);
    mockMvc.perform(MockMvcRequestBuilders
        .get("/api/orders")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));
  }
  
  @Test
  public void testCheckout_NoDiscounts() throws Exception {
    List<Long> watchIDs = Arrays.asList(1L, 2L, 3L);
    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/orders/checkout")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.mapper.writeValueAsString(watchIDs));
    
    mockMvc.perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.price", is(230.00)));
  }
  
  @Test
  public void testCheckout_Discount() throws Exception {
    List<Long> watchIDs = Arrays.asList(2L, 1L, 2L, 4L);
    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/orders/checkout")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.mapper.writeValueAsString(watchIDs));
    
    mockMvc.perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.price", is(250.00)));
  }
  
  @Test
  public void testCheckout_MultipleDiscount() throws Exception {
    List<Long> watchIDs = Arrays.asList(2L, 2L, 2L, 2L, 1L, 1L, 1L, 4L);
    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/orders/checkout")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.mapper.writeValueAsString(watchIDs));
    
    mockMvc.perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.price", is(470.0)));
  }
  
  @Test
  public void testCreate() throws Exception  {
    Watch watch = new Watch();
    watch.setId(3L);
    Mockito.when(watchService.getWatch(3L)).thenReturn(watch);
    WatchOrderDto watchOrderDto = new WatchOrderDto();
    watchOrderDto.setWatch(watch);
    watchOrderDto.setQuantity(2);
    List<WatchOrderDto> records = new ArrayList<>(Arrays.asList(watchOrderDto));
    OrderController.WatchOrderForm orderForm = new OrderController.WatchOrderForm();
    orderForm.setWatchOrders(records);
    
    Mockito.when(orderService.create(Mockito.any(Order.class))).thenAnswer(new Answer<Order>() {
      public Order answer(InvocationOnMock invocation) {
          Order order = (Order) invocation.getArguments()[0];
          order.setId(12345L);
          return order;
      }
    });
    
    Mockito.when(watchOrderService.create(Mockito.any(WatchOrder.class))).thenAnswer(new Answer<WatchOrder>() {
      public WatchOrder answer(InvocationOnMock invocation) {
          return (WatchOrder) invocation.getArguments()[0];
      }
    });
    
    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.mapper.writeValueAsString(orderForm));
 
    mockMvc.perform(mockRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("CREATED"))
        .andExpect(header().string("Location", endsWith("/api/orders/12345")))
        .andExpect(jsonPath("$.watchOrders", hasSize(1)))
        .andExpect(jsonPath("$.watchOrders[0].quantity").value("2"))
        .andExpect(jsonPath("$.watchOrders[0].watch.id").value("3"));
  }

}
