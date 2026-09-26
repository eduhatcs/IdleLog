package com.pucminas.roteiro.service;

import com.pucminas.roteiro.dto.GerenteRequest;
import com.pucminas.roteiro.dto.GerenteResponse;
import com.pucminas.roteiro.exception.BusinessRuleException;
import com.pucminas.roteiro.exception.ResourceNotFoundException;
import com.pucminas.roteiro.model.Gerente;
import com.pucminas.roteiro.repository.GerenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GerenteService {

    private final GerenteRepository repository;

    public GerenteResponse criar(GerenteRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new BusinessRuleException("Já existe um gerente cadastrado com este e-mail.");
        }
        Gerente gerente = new Gerente();
        aplicar(gerente, request);
        return toResponse(repository.save(gerente));
    }

    @Transactional(readOnly = true)
    public List<GerenteResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public GerenteResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public GerenteResponse atualizar(Long id, GerenteRequest request) {
        Gerente gerente = buscarEntidade(id);
        if (!gerente.getEmail().equals(request.email()) && repository.existsByEmail(request.email())) {
            throw new BusinessRuleException("Já existe um gerente cadastrado com este e-mail.");
        }
        aplicar(gerente, request);
        return toResponse(repository.save(gerente));
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Gerente não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    private Gerente buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gerente não encontrado: " + id));
    }

    private void aplicar(Gerente gerente, GerenteRequest request) {
        gerente.setNome(request.nome());
        gerente.setTelefone(request.telefone());
        gerente.setEmail(request.email());
    }

    private GerenteResponse toResponse(Gerente g) {
        return new GerenteResponse(g.getId(), g.getNome(), g.getTelefone(), g.getEmail());
    }
}
