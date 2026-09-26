package com.service;

import com.dto.PedidoRequest;
import com.dto.PedidoResponse;
import com.dto.PedidoStatusRequest;
import com.exception.ResourceNotFoundException;
import com.model.Cliente;
import com.model.Pedido;
import com.model.StatusPedido;
import com.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Cadastro de pedidos (RF - "Gerenciar Pedidos") e sua relação com o cliente
 * que o realizou ("realiza" no diagrama de classes).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PedidoService {

    private final PedidoRepository repository;
    private final ClienteService clienteService;

    public PedidoResponse criar(PedidoRequest request) {
        Cliente cliente = clienteService.buscarEntidade(request.clienteId());

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setData(request.data());
        pedido.setEnderecoEntrega(request.enderecoEntrega());
        pedido.setStatus(StatusPedido.PENDENTE);
        return toResponse(repository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public PedidoResponse atualizar(Long id, PedidoRequest request) {
        Pedido pedido = buscarEntidade(id);
        Cliente cliente = clienteService.buscarEntidade(request.clienteId());
        pedido.setCliente(cliente);
        pedido.setData(request.data());
        pedido.setEnderecoEntrega(request.enderecoEntrega());
        return toResponse(repository.save(pedido));
    }

    public PedidoResponse atualizarStatus(Long id, PedidoStatusRequest request) {
        Pedido pedido = buscarEntidade(id);
        pedido.setStatus(request.status());
        return toResponse(repository.save(pedido));
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    /** Pacote-privado: usado por RoteiroService para vincular um pedido ao ponto do roteiro. */
    Pedido buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));
    }

    private PedidoResponse toResponse(Pedido p) {
        return new PedidoResponse(p.getId(), p.getCliente().getId(), p.getCliente().getNome(),
                p.getData(), p.getEnderecoEntrega(), p.getStatus());
    }
}