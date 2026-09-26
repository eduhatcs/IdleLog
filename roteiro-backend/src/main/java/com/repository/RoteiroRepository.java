package com.repository;

import com.model.Roteiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RoteiroRepository extends JpaRepository<Roteiro, Long> {
    // RN05: cada roteiro pertence a um único motorista e a uma única data
    Optional<Roteiro> findByMotoristaIdAndData(Long motoristaId, LocalDate data);
}
