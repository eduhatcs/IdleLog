package com.pucminas.roteiro.repository;

import com.pucminas.roteiro.model.Ponto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PontoRepository extends JpaRepository<Ponto, Long> {
    List<Ponto> findByRoteiroIdOrderByOrdemAsc(Long roteiroId);
    long countByRoteiroId(Long roteiroId);
}
