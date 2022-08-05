package com.bcgdv.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bcgdv.backend.model.Watch;

public interface WatchRepository extends JpaRepository<Watch, Long> {
}