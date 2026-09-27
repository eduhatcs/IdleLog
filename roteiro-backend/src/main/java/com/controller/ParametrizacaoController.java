package com.controller;

import com.dto.CustoRoteiroResponse;
import com.dto.ParametrizacaoRequest;
import com.dto.ParametrizacaoResponse;
import com.service.ParametrizacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parametrizacao")
public class ParametrizacaoController {

    private final ParametrizacaoService parametrizacaoService;

    public ParametrizacaoController(ParametrizacaoService parametrizacaoService) {
        this.parametrizacaoService = parametrizacaoService;
    }

    @GetMapping
    public ResponseEntity<ParametrizacaoResponse> buscarParametrizacao() {
        return ResponseEntity.ok(parametrizacaoService.buscarParametrizacao());
    }

    @PutMapping
    public ResponseEntity<ParametrizacaoResponse> atualizarParametrizacao(@Valid @RequestBody ParametrizacaoRequest request) {
        return ResponseEntity.ok(parametrizacaoService.salvarOuAtualizar(request));
    }

    @GetMapping("/roteiros/{id}/custo")
    public ResponseEntity<CustoRoteiroResponse> calcularCustoRoteiro(@PathVariable Long id) {
        return ResponseEntity.ok(parametrizacaoService.calcularCustoRoteiro(id));
    }
}