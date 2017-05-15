package com.bestdeals.returnscalculator.persistence;

import com.bestdeals.returnscalculator.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {}
