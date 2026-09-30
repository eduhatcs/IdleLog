package com.service;

import com.dto.CustoRoteiroResponse;
import com.dto.ParametrizacaoRequest;
import com.dto.ParametrizacaoResponse;
import com.exception.ResourceNotFoundException;
import com.model.Motorista;
import com.model.Parametrizacao;
import com.model.Ponto;
import com.model.Roteiro;
import com.repository.ParametrizacaoRepository;
import com.repository.RoteiroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ParametrizacaoService {

    private final ParametrizacaoRepository parametrizacaoRepository;
    private final RoteiroRepository roteiroRepository;

    public ParametrizacaoService(ParametrizacaoRepository parametrizacaoRepository, RoteiroRepository roteiroRepository) {
        this.parametrizacaoRepository = parametrizacaoRepository;
        this.roteiroRepository = roteiroRepository;
    }

    @Transactional
    public Parametrizacao getParametrizacaoAtual() {
        List<Parametrizacao> lista = parametrizacaoRepository.findAll();
        if (!lista.isEmpty()) {
            return lista.get(0);
        }
        Parametrizacao padrao = new Parametrizacao(
                null,
                BigDecimal.valueOf(5.89),
                BigDecimal.valueOf(12.0),
                BigDecimal.valueOf(8.0),
                BigDecimal.valueOf(25.0)
        );
        return parametrizacaoRepository.save(padrao);
    }

    @Transactional
    public ParametrizacaoResponse buscarParametrizacao() {
        return toResponse(getParametrizacaoAtual());
    }

    @Transactional
    public ParametrizacaoResponse salvarOuAtualizar(ParametrizacaoRequest request) {
        List<Parametrizacao> lista = parametrizacaoRepository.findAll();
        Parametrizacao p = lista.isEmpty() ? new Parametrizacao() : lista.get(0);

        p.setPrecoCombustivel(request.getPrecoCombustivel());
        p.setKmPorLitroPadrao(request.getKmPorLitroPadrao());
        p.setJornadaMaximaHoras(request.getJornadaMaximaHoras());
        p.setCustoHoraParada(request.getCustoHoraParada() != null ? request.getCustoHoraParada() : BigDecimal.ZERO);

        Parametrizacao salvo = parametrizacaoRepository.save(p);
        return toResponse(salvo);
    }

    /**
     * RN07: Cálculo de custo e análise de jornada do roteiro
     */
    @Transactional
    public CustoRoteiroResponse calcularCustoRoteiro(Long roteiroId) {
        Roteiro roteiro = roteiroRepository.findById(roteiroId)
                .orElseThrow(() -> new ResourceNotFoundException("Roteiro não encontrado com id: " + roteiroId));

        Parametrizacao param = getParametrizacaoAtual();
        Motorista motorista = roteiro.getMotorista();

        // Conversão de rendimentoKmLitro (Double) para BigDecimal
        BigDecimal rendimento = (motorista != null && motorista.getRendimentoKmLitro() != null && motorista.getRendimentoKmLitro() > 0)
                ? BigDecimal.valueOf(motorista.getRendimentoKmLitro())
                : param.getKmPorLitroPadrao();

        // Conversão de distanciaTotalKm (Double) para BigDecimal
        BigDecimal distancia = (roteiro.getDistanciaTotalKm() != null)
                ? BigDecimal.valueOf(roteiro.getDistanciaTotalKm())
                : BigDecimal.ZERO;

        BigDecimal precoCombustivel = param.getPrecoCombustivel();

        // Consumo em litros = Distância / Rendimento
        BigDecimal consumoLitros = BigDecimal.ZERO;
        if (rendimento.compareTo(BigDecimal.ZERO) > 0) {
            consumoLitros = distancia.divide(rendimento, 2, RoundingMode.HALF_UP);
        }

        // Custo Combustível = Consumo Litros * Preço Combustível
        BigDecimal custoCombustivel = consumoLitros.multiply(precoCombustivel).setScale(2, RoundingMode.HALF_UP);

        // Tempo parado no Roteiro é Long
        Long minutosParado = (roteiro.getTempoTotalParadoMinutos() != null) ? roteiro.getTempoTotalParadoMinutos() : 0L;
        BigDecimal horasParadas = BigDecimal.valueOf(minutosParado).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        BigDecimal custoHoraParada = param.getCustoHoraParada() != null ? param.getCustoHoraParada() : BigDecimal.ZERO;
        BigDecimal custoTempoParado = horasParadas.multiply(custoHoraParada).setScale(2, RoundingMode.HALF_UP);

        BigDecimal custoTotalEstimado = custoCombustivel.add(custoTempoParado).setScale(2, RoundingMode.HALF_UP);

        // Validação de jornada de trabalho (duração total entre primeiro e último ponto)
        boolean jornadaExcedida = false;
        List<Ponto> pontos = new ArrayList<>(roteiro.getPontos());
        if (pontos != null && pontos.size() >= 2) {
            pontos.sort(Comparator.comparingInt(Ponto::getOrdem));
            LocalDateTime inicio = pontos.get(0).getDataHoraChegada();
            Ponto ultimo = pontos.get(pontos.size() - 1);
            LocalDateTime fim = ultimo.getDataHoraSaida() != null ? ultimo.getDataHoraSaida() : ultimo.getDataHoraChegada();

            if (inicio != null && fim != null) {
                long minutosJornada = Duration.between(inicio, fim).toMinutes();
                BigDecimal horasJornada = BigDecimal.valueOf(minutosJornada).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
                if (horasJornada.compareTo(param.getJornadaMaximaHoras()) > 0) {
                    jornadaExcedida = true;
                }
            }
        }

        CustoRoteiroResponse response = new CustoRoteiroResponse();
        response.setRoteiroId(roteiro.getId());
        response.setData(roteiro.getData() != null ? roteiro.getData().toString() : null);
        response.setMotoristaId(motorista != null ? motorista.getId() : null);
        response.setMotoristaNome(motorista != null ? motorista.getNome() : "Desconhecido");
        response.setDistanciaTotalKm(distancia);
        response.setRendimentoKmLitro(rendimento);
        response.setPrecoCombustivel(precoCombustivel);
        response.setConsumoLitros(consumoLitros);
        response.setCustoCombustivel(custoCombustivel);
        response.setTempoTotalParadoMinutos(minutosParado);
        response.setTempoTotalParadoHoras(horasParadas);
        response.setCustoHoraParada(custoHoraParada);
        response.setCustoTempoParado(custoTempoParado);
        response.setCustoTotalEstimado(custoTotalEstimado);
        response.setJornadaMaximaHoras(param.getJornadaMaximaHoras());
        response.setJornadaExcedida(jornadaExcedida);

        return response;
    }

    private ParametrizacaoResponse toResponse(Parametrizacao p) {
        return new ParametrizacaoResponse(
                p.getId(),
                p.getPrecoCombustivel(),
                p.getKmPorLitroPadrao(),
                p.getJornadaMaximaHoras(),
                p.getCustoHoraParada()
        );
    }
}