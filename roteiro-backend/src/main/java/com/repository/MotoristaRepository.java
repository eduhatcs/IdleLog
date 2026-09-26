package com.repository;

import com.model.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    boolean existsByDocumento(String documento);
}
