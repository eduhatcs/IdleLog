package com.repository;

import com.model.Parametrizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametrizacaoRepository extends JpaRepository<Parametrizacao, Long> {
}