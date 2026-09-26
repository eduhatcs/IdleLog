package com.service;

import com.dto.ClienteRequest;
import com.dto.ClienteResponse;
import com.exception.BusinessRuleException;
import com.exception.ResourceNotFoundException;
import com.model.Cliente;
import com.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteResponse criar(ClienteRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new BusinessRuleException("Já existe um cliente cadastrado com este e-mail.");
        }
        Cliente cliente = new Cliente();
        aplicar(cliente, request);
        return toResponse(repository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarEntidade(id);
        if (!cliente.getEmail().equals(request.email()) && repository.existsByEmail(request.email())) {
            throw new BusinessRuleException("Já existe um cliente cadastrado com este e-mail.");
        }
        aplicar(cliente, request);
        return toResponse(repository.save(cliente));
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    /** Pacote-privado: usado por PedidoService para validar e vincular o cliente ao pedido. */
    Cliente buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + id));
    }

    private void aplicar(Cliente cliente, ClienteRequest request) {
        cliente.setNome(request.nome());
        cliente.setTelefone(request.telefone());
        cliente.setEmail(request.email());
    }

    private ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(c.getId(), c.getNome(), c.getTelefone(), c.getEmail());
    }
}