package com.repository;

import com.model.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GerenteRepository extends JpaRepository<Gerente, Long> {
    boolean existsByEmail(String email);
}
