package com.java.EcoDrive.service;

import com.java.EcoDrive.dto.StatusEstacaoRecarga.StatusEstacaoRecargaCreateDTO;
import com.java.EcoDrive.exception.ResourceNotFoundException;
import com.java.EcoDrive.model.EstacaoRecarga;
import com.java.EcoDrive.model.StatusEstacaoRecarga;
import com.java.EcoDrive.repository.EstacaoRecargaRepository;
import com.java.EcoDrive.repository.StatusEstacaoRecargaRepository;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public StatusEstacaoRecargaService(StatusEstacaoRecargaRepository statusRepository,
                                       EstacaoRecargaRepository estacaoRecargaRepository,
                                       ModelMapper modelMapper) {
        this.statusRepository = statusRepository;
        this.estacaoRecargaRepository = estacaoRecargaRepository;
        this.modelMapper = modelMapper;
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

    public StatusEstacaoRecarga criarStatusEstacao(StatusEstacaoRecargaCreateDTO statusDTO) {
        EstacaoRecarga estacao = estacaoRecargaRepository.findById(statusDTO.getEstacaoId())
                .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_NAO_ENCONTRADA + statusDTO.getEstacaoId()));

        StatusEstacaoRecarga status = modelMapper.map(statusDTO, StatusEstacaoRecarga.class);
        status.setEstacaoRecarga(estacao);
        status.setUltimaAtualizacao(LocalDateTime.now());

        return statusRepository.save(status);
    }

    public StatusEstacaoRecarga atualizarStatus(Long id, StatusEstacaoRecargaCreateDTO statusDTO) {
        StatusEstacaoRecarga statusExistente = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STATUS_NAO_ENCONTRADO + id));

        if (statusDTO.getEstacaoId() != null) {
            EstacaoRecarga estacao = estacaoRecargaRepository.findById(statusDTO.getEstacaoId())
                    .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_NAO_ENCONTRADA + statusDTO.getEstacaoId()));
            statusExistente.setEstacaoRecarga(estacao);
        }

        if (statusDTO.getStatus() != null) {
            statusExistente.setStatus(statusDTO.getStatus());
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
