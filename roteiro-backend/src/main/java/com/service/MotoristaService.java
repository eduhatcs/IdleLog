package com.service;

import com.dto.MotoristaRequest;
import com.dto.MotoristaResponse;
import com.exception.BusinessRuleException;
import com.exception.ResourceNotFoundException;
import com.model.Motorista;
import com.repository.MotoristaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MotoristaService {

    private final MotoristaRepository repository;

    public MotoristaResponse criar(MotoristaRequest request) {
        if (repository.existsByDocumento(request.documento())) {
            throw new BusinessRuleException("Já existe um motorista cadastrado com este documento.");
        }
        Motorista motorista = new Motorista();
        aplicar(motorista, request);
        return toResponse(repository.save(motorista));
    }

    @Transactional(readOnly = true)
    public List<MotoristaResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MotoristaResponse buscar(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public MotoristaResponse atualizar(Long id, MotoristaRequest request) {
        Motorista motorista = buscarEntidade(id);
        if (!motorista.getDocumento().equals(request.documento())
                && repository.existsByDocumento(request.documento())) {
            throw new BusinessRuleException("Já existe um motorista cadastrado com este documento.");
        }
        aplicar(motorista, request);
        return toResponse(repository.save(motorista));
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Motorista não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    /** Pacote-privado: usado por RoteiroService para validar e vincular o motorista ao roteiro. */
    Motorista buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorista não encontrado: " + id));
    }

    private void aplicar(Motorista motorista, MotoristaRequest request) {
        motorista.setNome(request.nome());
        motorista.setTelefone(request.telefone());
        motorista.setDocumento(request.documento());
        motorista.setVeiculo(request.veiculo());
        motorista.setRendimentoKmLitro(request.rendimentoKmLitro());
    }

    private MotoristaResponse toResponse(Motorista m) {
        return new MotoristaResponse(m.getId(), m.getNome(), m.getTelefone(), m.getDocumento(),
                m.getVeiculo(), m.getRendimentoKmLitro());
    }
}
