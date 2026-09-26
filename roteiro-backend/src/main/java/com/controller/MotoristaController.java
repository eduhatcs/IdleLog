package com.pucminas.roteiro.controller;

import com.pucminas.roteiro.dto.MotoristaRequest;
import com.pucminas.roteiro.dto.MotoristaResponse;
import com.pucminas.roteiro.service.MotoristaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motoristas")
@RequiredArgsConstructor
public class MotoristaController {

    private final MotoristaService service;

    @PostMapping
    public ResponseEntity<MotoristaResponse> criar(@Valid @RequestBody MotoristaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<MotoristaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public MotoristaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public MotoristaResponse atualizar(@PathVariable Long id, @Valid @RequestBody MotoristaRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
