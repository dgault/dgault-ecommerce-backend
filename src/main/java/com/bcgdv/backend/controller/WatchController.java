package com.bcgdv.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcgdv.backend.model.Watch;
import com.bcgdv.backend.service.WatchService;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/watches")
public class WatchController {

  private WatchService watchService;

  public WatchController(WatchService watchService) {
    this.watchService = watchService;
  }

  @GetMapping(value = { "", "/" })
  public @NotNull Iterable<Watch> getWatches() {
    return watchService.getAllWatches();
  }
}