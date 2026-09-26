package com.controller;

import com.dto.GerenteRequest;
import com.dto.GerenteResponse;
import com.service.GerenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gerentes")
@RequiredArgsConstructor
public class GerenteController {

    private final GerenteService service;

    @PostMapping
    public ResponseEntity<GerenteResponse> criar(@Valid @RequestBody GerenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<GerenteResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public GerenteResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public GerenteResponse atualizar(@PathVariable Long id, @Valid @RequestBody GerenteRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
