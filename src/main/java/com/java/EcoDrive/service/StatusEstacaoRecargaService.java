package com.java.EcoDrive.service;

import com.java.EcoDrive.exception.ResourceNotFoundException;
import com.java.EcoDrive.model.EstacaoRecarga;
import com.java.EcoDrive.model.StatusEstacaoRecarga;
import com.java.EcoDrive.repository.EstacaoRecargaRepository;
import com.java.EcoDrive.repository.StatusEstacaoRecargaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatusEstacaoRecargaService {

    private static final String STATUS_NAO_ENCONTRADO = "Status não encontrado com ID: ";
    private static final String ESTACAO_NAO_ENCONTRADA = "Estação de recarga não encontrada com ID: ";

    private final StatusEstacaoRecargaRepository statusRepository;
    private final EstacaoRecargaRepository estacaoRecargaRepository;

    @Autowired
    public StatusEstacaoRecargaService(StatusEstacaoRecargaRepository statusRepository,
                                       EstacaoRecargaRepository estacaoRecargaRepository) {
        this.statusRepository = statusRepository;
        this.estacaoRecargaRepository = estacaoRecargaRepository;
    }

    public Page<StatusEstacaoRecarga> listarTodosPaginado(Pageable pageable) {
        return statusRepository.findAll(pageable);
    }

    public List<StatusEstacaoRecarga> listarTodos() {
        return statusRepository.findAll();
    }

    public StatusEstacaoRecarga obterPorId(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STATUS_NAO_ENCONTRADO + id));
    }

    public List<StatusEstacaoRecarga> listarPorEstacao(Long estacaoId) {
        EstacaoRecarga estacao = estacaoRecargaRepository.findById(estacaoId)
                .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_NAO_ENCONTRADA + estacaoId));

        return statusRepository.findByEstacaoRecarga(estacao);
    }

    public StatusEstacaoRecarga criarStatus(StatusEstacaoRecarga status) {
        Long estacaoId = status.getEstacaoRecarga().getEstacaoId();
        EstacaoRecarga estacao = estacaoRecargaRepository.findById(estacaoId)
                .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_NAO_ENCONTRADA + estacaoId));

        status.setEstacaoRecarga(estacao);
        status.setUltimaAtualizacao(LocalDateTime.now());
        return statusRepository.save(status);
    }

    public StatusEstacaoRecarga atualizarStatus(Long id, StatusEstacaoRecarga statusAtualizado) {
        StatusEstacaoRecarga statusExistente = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STATUS_NAO_ENCONTRADO + id));

        if (statusAtualizado.getStatus() != null) {
            statusExistente.setStatus(statusAtualizado.getStatus());
        }
        statusExistente.setUltimaAtualizacao(LocalDateTime.now());

        return statusRepository.save(statusExistente);
    }

    public void deletarStatus(Long id) {
        StatusEstacaoRecarga status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STATUS_NAO_ENCONTRADO + id));
        statusRepository.delete(status);
    }
}
