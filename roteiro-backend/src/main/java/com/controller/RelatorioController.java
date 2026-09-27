package com.controller;

import com.dto.CustoRoteiroResponse;
import com.model.Ponto;
import com.model.Roteiro;
import com.repository.RoteiroRepository;
import com.service.ParametrizacaoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RoteiroRepository roteiroRepository;
    private final ParametrizacaoService parametrizacaoService;

    public RelatorioController(RoteiroRepository roteiroRepository, ParametrizacaoService parametrizacaoService) {
        this.roteiroRepository = roteiroRepository;
        this.parametrizacaoService = parametrizacaoService;
    }

    @GetMapping(value = "/roteiros/csv", produces = "text/csv; charset=UTF-8")
    public ResponseEntity<byte[]> exportarRoteirosCsv() {
        List<Roteiro> roteiros = roteiroRepository.findAll();
        StringBuilder csv = new StringBuilder();
        csv.append("ID;Data;Motorista;Distancia (km);Tempo Parado (min);Custo Combustivel (R$);Custo Parada (R$);Custo Total (R$);Jornada Excedida\n");

        for (Roteiro r : roteiros) {
            CustoRoteiroResponse c = parametrizacaoService.calcularCustoRoteiro(r.getId());
            csv.append(r.getId()).append(";")
               .append(r.getData()).append(";")
               .append(c.getMotoristaNome()).append(";")
               .append(c.getDistanciaTotalKm()).append(";")
               .append(c.getTempoTotalParadoMinutos()).append(";")
               .append(c.getCustoCombustivel()).append(";")
               .append(c.getCustoTempoParado()).append(";")
               .append(c.getCustoTotalEstimado()).append(";")
               .append(Boolean.TRUE.equals(c.getJornadaExcedida()) ? "SIM" : "NAO")
               .append("\n");
        }

        byte[] bytes = ("\uFEFF" + csv).getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio_roteiros.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(bytes);
    }
}