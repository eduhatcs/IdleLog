package com.pucminas.roteiro.repository;

import com.pucminas.roteiro.model.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GerenteRepository extends JpaRepository<Gerente, Long> {
    boolean existsByEmail(String email);
}
