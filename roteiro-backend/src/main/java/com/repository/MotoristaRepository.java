package com.pucminas.roteiro.repository;

import com.pucminas.roteiro.model.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    boolean existsByDocumento(String documento);
}
