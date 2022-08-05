package com.bcgdv.backend.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.bcgdv.backend.model.Watch;
import com.bcgdv.backend.service.WatchService;

public class WatchControllerTest {

  private WatchController watchController;

  @Mock
  private WatchService mockWatchService;
  
  @Mock
  private Iterable<Watch> mockWatchList;
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    watchController = new WatchController(mockWatchService);
  }
 
  @Test
  public void testGetWatches() {
    Mockito.when(mockWatchService.getAllWatches()).thenReturn(mockWatchList);
    assertEquals(mockWatchList, watchController.getWatches());
  }

}
