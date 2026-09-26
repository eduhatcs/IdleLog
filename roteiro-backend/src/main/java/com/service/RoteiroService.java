package com.pucminas.roteiro.service;

import com.pucminas.roteiro.dto.PontoRequest;
import com.pucminas.roteiro.dto.PontoResponse;
import com.pucminas.roteiro.dto.PontoSaidaRequest;
import com.pucminas.roteiro.dto.RoteiroRequest;
import com.pucminas.roteiro.dto.RoteiroResponse;
import com.pucminas.roteiro.exception.BusinessRuleException;
import com.pucminas.roteiro.exception.ResourceNotFoundException;
import com.pucminas.roteiro.model.Motorista;
import com.pucminas.roteiro.model.Ponto;
import com.pucminas.roteiro.model.Roteiro;
import com.pucminas.roteiro.repository.PontoRepository;
import com.pucminas.roteiro.repository.RoteiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 * Monta e mantém os roteiros: cadastro do roteiro (motorista + data) e
 * inclusão sequencial dos pontos, aplicando RN01, RN02, RN03, RN05 e RN06.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RoteiroService {

    private final RoteiroRepository roteiroRepository;
    private final PontoRepository pontoRepository;
    private final MotoristaService motoristaService;

    public RoteiroResponse criar(RoteiroRequest request) {
        Motorista motorista = motoristaService.buscarEntidade(request.motoristaId());

        // RN05: cada roteiro pertence a um único motorista e a uma única data
        roteiroRepository.findByMotoristaIdAndData(motorista.getId(), request.data())
                .ifPresent(r -> {
                    throw new BusinessRuleException(
                            "Este motorista já possui um roteiro cadastrado para a data informada.");
                });

        Roteiro roteiro = new Roteiro();
        roteiro.setMotorista(motorista);
        roteiro.setData(request.data());
        roteiro.setDistanciaTotalKm(request.distanciaTotalKm());
        roteiro.setTempoTotalParadoMinutos(0L);
        return toResponse(roteiroRepository.save(roteiro));
    }

    @Transactional(readOnly = true)
    public List<RoteiroResponse> listar() {
        return roteiroRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RoteiroResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public PontoResponse adicionarPonto(Long roteiroId, PontoRequest request) {
        Roteiro roteiro = buscarEntidade(roteiroId);

        // RN06: os pontos de um roteiro possuem ordem sequencial (1, 2, 3, 4...)
        long proximaOrdem = pontoRepository.countByRoteiroId(roteiroId) + 1;
        if (!request.ordem().equals((int) proximaOrdem)) {
            throw new BusinessRuleException(
                    "Ordem inválida: o próximo ponto deste roteiro deve ter ordem " + proximaOrdem + ".");
        }

        if (request.dataHoraSaida() != null
                && request.dataHoraSaida().isBefore(request.dataHoraChegada())) {
            throw new BusinessRuleException("A data/hora de saída não pode ser anterior à de chegada.");
        }

        Ponto ponto = new Ponto();
        ponto.setEndereco(request.endereco());
        ponto.setLatitude(request.latitude());
        ponto.setLongitude(request.longitude());
        ponto.setOrdem(request.ordem());
        ponto.setDataHoraChegada(request.dataHoraChegada());
        ponto.setDataHoraSaida(request.dataHoraSaida());
        ponto.setRoteiro(roteiro);
        ponto.setTempoParadoMinutos(calcularTempoParado(ponto));

        Ponto salvo = pontoRepository.save(ponto);
        recalcularTempoTotalParado(roteiro);
        return toResponse(salvo);
    }

    public PontoResponse registrarSaida(Long roteiroId, Long pontoId, PontoSaidaRequest request) {
        Roteiro roteiro = buscarEntidade(roteiroId);
        Ponto ponto = pontoRepository.findById(pontoId)
                .filter(p -> p.getRoteiro().getId().equals(roteiroId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ponto não encontrado no roteiro " + roteiroId + ": " + pontoId));

        if (request.dataHoraSaida().isBefore(ponto.getDataHoraChegada())) {
            throw new BusinessRuleException("A data/hora de saída não pode ser anterior à de chegada.");
        }

        ponto.setDataHoraSaida(request.dataHoraSaida());
        ponto.setTempoParadoMinutos(calcularTempoParado(ponto));
        Ponto salvo = pontoRepository.save(ponto);
        recalcularTempoTotalParado(roteiro);
        return toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<PontoResponse> listarPontos(Long roteiroId) {
        buscarEntidade(roteiroId);
        return pontoRepository.findByRoteiroIdOrderByOrdemAsc(roteiroId).stream()
                .map(this::toResponse).toList();
    }

    /** RN01: o ponto de partida (ordem 1) não conta tempo parado. RN02: diferença saída - chegada. */
    private Long calcularTempoParado(Ponto ponto) {
        if (ponto.getOrdem() != null && ponto.getOrdem() == 1) {
            return null;
        }
        if (ponto.getDataHoraSaida() == null) {
            return null; // ainda parado / saída ainda não registrada
        }
        return Duration.between(ponto.getDataHoraChegada(), ponto.getDataHoraSaida()).toMinutes();
    }

    /** RN03: tempo total parado do roteiro = soma dos pontos, exceto o ponto de partida. */
    private void recalcularTempoTotalParado(Roteiro roteiro) {
        List<Ponto> pontos = pontoRepository.findByRoteiroIdOrderByOrdemAsc(roteiro.getId());
        long total = pontos.stream()
                .filter(p -> p.getOrdem() != 1 && p.getTempoParadoMinutos() != null)
                .mapToLong(Ponto::getTempoParadoMinutos)
                .sum();
        roteiro.setTempoTotalParadoMinutos(total);
        roteiroRepository.save(roteiro);
    }

    Roteiro buscarEntidade(Long id) {
        return roteiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Roteiro não encontrado: " + id));
    }

    private RoteiroResponse toResponse(Roteiro r) {
        List<PontoResponse> pontos = pontoRepository.findByRoteiroIdOrderByOrdemAsc(r.getId()).stream()
                .map(this::toResponse).toList();
        return new RoteiroResponse(r.getId(), r.getData(), r.getMotorista().getId(),
                r.getMotorista().getNome(), r.getDistanciaTotalKm(), r.getTempoTotalParadoMinutos(), pontos);
    }

    private PontoResponse toResponse(Ponto p) {
        return new PontoResponse(p.getId(), p.getEndereco(), p.getLatitude(), p.getLongitude(),
                p.getOrdem(), p.getDataHoraChegada(), p.getDataHoraSaida(), p.getTempoParadoMinutos());
    }
}
