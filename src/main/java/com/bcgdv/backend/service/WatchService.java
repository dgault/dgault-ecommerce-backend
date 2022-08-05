package com.bcgdv.backend.service;

import com.bcgdv.backend.model.Watch;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
public interface WatchService {

  @NotNull Iterable<Watch> getAllWatches();

  Watch getWatch(@Min(value = 1L, message = "Invalid watch ID.") long id);

  Watch save(Watch watch);
}