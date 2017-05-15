package com.bestdeals.returnscalculator.persistence;

import com.bestdeals.returnscalculator.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {}
