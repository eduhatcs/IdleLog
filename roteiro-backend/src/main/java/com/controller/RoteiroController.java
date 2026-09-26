package com.controller;

import com.dto.PontoRequest;
import com.dto.PontoResponse;
import com.dto.PontoSaidaRequest;
import com.dto.RoteiroRequest;
import com.dto.RoteiroResponse;
import com.service.RoteiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roteiros")
@RequiredArgsConstructor
public class RoteiroController {

    private final RoteiroService service;

    @PostMapping
    public ResponseEntity<RoteiroResponse> criar(@Valid @RequestBody RoteiroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<RoteiroResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public RoteiroResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping("/{id}/pontos")
    public ResponseEntity<PontoResponse> adicionarPonto(@PathVariable Long id,
                                                          @Valid @RequestBody PontoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarPonto(id, request));
    }

    @GetMapping("/{id}/pontos")
    public List<PontoResponse> listarPontos(@PathVariable Long id) {
        return service.listarPontos(id);
    }

    @PatchMapping("/{id}/pontos/{pontoId}/saida")
    public PontoResponse registrarSaida(@PathVariable Long id, @PathVariable Long pontoId,
                                          @Valid @RequestBody PontoSaidaRequest request) {
        return service.registrarSaida(id, pontoId, request);
    }
}
