package com.java.EcoDrive.service;

import com.java.EcoDrive.dto.EstacaoRecarga.EstacaoRecargaCreateDTO;
import com.java.EcoDrive.dto.EstacaoRecarga.EstacaoRecargaDTO;
import com.java.EcoDrive.exception.ResourceNotFoundException;
import com.java.EcoDrive.exception.InvalidRequestException;
import com.java.EcoDrive.model.Bairro;
import com.java.EcoDrive.model.EstacaoRecarga;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.java.EcoDrive.repository.BairroRepository;
import com.java.EcoDrive.repository.EstacaoRecargaRepository;

import java.util.List;

@Service
public class EstacaoRecargaService {

    private static final String ESTACAO_RECARGA_NAO_ENCONTRADA = "Estação de recarga não encontrada com ID: ";
    private static final String BAIRRO_NAO_ENCONTRADO = "Bairro não encontrado com ID: ";
    private static final String NENHUMA_ESTACAO_ENCONTRADA_BAIRRO = "Nenhuma estação de recarga encontrada para o bairro com ID: ";
    private static final String NENHUMA_ESTACAO_ENCONTRADA_TIPO = "Nenhuma estação de recarga encontrada com o tipo de carregador: ";

    private final EstacaoRecargaRepository estacaoRecargaRepository;
    private final BairroRepository bairroRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EstacaoRecargaService(EstacaoRecargaRepository estacaoRecargaRepository, BairroRepository bairroRepository, ModelMapper modelMapper) {
        this.estacaoRecargaRepository = estacaoRecargaRepository;
        this.bairroRepository = bairroRepository;
        this.modelMapper = modelMapper;
    }

    public Page<EstacaoRecargaDTO> listarTodosPaginado(Pageable pageable) {
        Page<EstacaoRecarga> estacoes = estacaoRecargaRepository.findAll(pageable);
        return estacoes.map(estacao -> modelMapper.map(estacao, EstacaoRecargaDTO.class));
    }

    public EstacaoRecargaDTO obterPorId(Long id) {
        EstacaoRecarga estacao = estacaoRecargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_RECARGA_NAO_ENCONTRADA + id));
        return modelMapper.map(estacao, EstacaoRecargaDTO.class);
    }

    public EstacaoRecargaDTO criarEstacaoRecarga(EstacaoRecargaCreateDTO estacaoCreateDTO) {
        if (estacaoCreateDTO.getNome() == null || estacaoCreateDTO.getNome().isEmpty()) {
            throw new InvalidRequestException("Nome da estação de recarga é obrigatório.");
        }

        Bairro bairro = bairroRepository.findById(estacaoCreateDTO.getBairroId())
                .orElseThrow(() -> new ResourceNotFoundException(BAIRRO_NAO_ENCONTRADO + estacaoCreateDTO.getBairroId()));

        if (estacaoCreateDTO.getLatitude() == null || estacaoCreateDTO.getLongitude() == null) {
            throw new InvalidRequestException("Latitude e Longitude são obrigatórios.");
        }

        EstacaoRecarga estacaoRecarga = modelMapper.map(estacaoCreateDTO, EstacaoRecarga.class);
        estacaoRecarga.setBairro(bairro);

        EstacaoRecarga estacaoSalva = estacaoRecargaRepository.save(estacaoRecarga);
        return modelMapper.map(estacaoSalva, EstacaoRecargaDTO.class);
    }

    public EstacaoRecargaDTO atualizarEstacaoRecarga(Long id, EstacaoRecargaCreateDTO estacaoCreateDTO) {
        EstacaoRecarga estacaoExistente = estacaoRecargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_RECARGA_NAO_ENCONTRADA + id));

        if (estacaoCreateDTO.getNome() != null && estacaoCreateDTO.getNome().isEmpty()) {
            throw new InvalidRequestException("Nome da estação de recarga não pode ser vazio.");
        }

        estacaoExistente.setNome(estacaoCreateDTO.getNome());
        estacaoExistente.setLatitude(estacaoCreateDTO.getLatitude());
        estacaoExistente.setLongitude(estacaoCreateDTO.getLongitude());
        estacaoExistente.setTipoCarregador(estacaoCreateDTO.getTipoCarregador());
        estacaoExistente.setPrecoPorKwh(estacaoCreateDTO.getPrecoPorKwh());

        if (estacaoCreateDTO.getBairroId() != null && !estacaoCreateDTO.getBairroId().equals(estacaoExistente.getBairro().getBairroId())) {
            Bairro novoBairro = bairroRepository.findById(estacaoCreateDTO.getBairroId())
                    .orElseThrow(() -> new ResourceNotFoundException(BAIRRO_NAO_ENCONTRADO + estacaoCreateDTO.getBairroId()));
            estacaoExistente.setBairro(novoBairro);
        }

        EstacaoRecarga estacaoAtualizada = estacaoRecargaRepository.save(estacaoExistente);
        return modelMapper.map(estacaoAtualizada, EstacaoRecargaDTO.class);
    }

    public void deletarEstacaoRecarga(Long id) {
        EstacaoRecarga estacaoRecarga = estacaoRecargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_RECARGA_NAO_ENCONTRADA + id));
        estacaoRecargaRepository.delete(estacaoRecarga);
    }

    public List<EstacaoRecargaDTO> listarPorBairro(Long bairroId) {
        List<EstacaoRecarga> estacoes = estacaoRecargaRepository.findByBairroBairroId(bairroId);
        if (estacoes.isEmpty()) {
            throw new ResourceNotFoundException(NENHUMA_ESTACAO_ENCONTRADA_BAIRRO + bairroId);
        }
        return estacoes.stream()
                .map(estacao -> modelMapper.map(estacao, EstacaoRecargaDTO.class))
                .toList();
    }

    public List<EstacaoRecargaDTO> listarPorTipoCarregador(String tipoCarregador) {
        List<EstacaoRecarga> estacoes = estacaoRecargaRepository.findByTipoCarregadorContainingIgnoreCase(tipoCarregador);
        if (estacoes.isEmpty()) {
            throw new ResourceNotFoundException(NENHUMA_ESTACAO_ENCONTRADA_TIPO + tipoCarregador);
        }
        return estacoes.stream()
                .map(estacao -> modelMapper.map(estacao, EstacaoRecargaDTO.class))
                .toList();
    }
}