package com.bcgdv.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcgdv.backend.exception.ResourceNotFoundException;
import com.bcgdv.backend.model.Watch;
import com.bcgdv.backend.repository.WatchRepository;

@Service
@Transactional
public class WatchServiceImpl implements WatchService {

  private WatchRepository watchRepository;

  public WatchServiceImpl(WatchRepository watchRepository) {
    this.watchRepository = watchRepository;
  }

  @Override
  public Iterable<Watch> getAllWatches() {
    return watchRepository.findAll();
  }

  @Override
  public Watch getWatch(long id) {
    return watchRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Watch not found"));
  }

  @Override
  public Watch save(Watch watch) {
    return watchRepository.save(watch);
  }
}