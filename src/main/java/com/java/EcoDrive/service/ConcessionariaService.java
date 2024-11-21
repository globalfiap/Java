package com.java.EcoDrive.service;

import com.java.EcoDrive.dto.Concessionaria.ConcessionariaCreateDTO;
import com.java.EcoDrive.dto.Concessionaria.ConcessionariaDTO;
import com.java.EcoDrive.exception.ResourceNotFoundException;
import com.java.EcoDrive.exception.InvalidRequestException;
import com.java.EcoDrive.model.Bairro;
import com.java.EcoDrive.model.Concessionaria;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.java.EcoDrive.repository.BairroRepository;
import com.java.EcoDrive.repository.ConcessionariaRepository;

import java.util.List;

@Service
public class ConcessionariaService {

    private static final String CONCESSIONARIA_NAO_ENCONTRADA = "Concessionária não encontrada com ID: ";

    private final ConcessionariaRepository concessionariaRepository;
    private final BairroRepository bairroRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ConcessionariaService(ConcessionariaRepository concessionariaRepository, BairroRepository bairroRepository, ModelMapper modelMapper) {
        this.concessionariaRepository = concessionariaRepository;
        this.bairroRepository = bairroRepository;
        this.modelMapper = modelMapper;
    }

    public Page<ConcessionariaDTO> listarTodosPaginado(Pageable pageable) {
        Page<Concessionaria> concessionarias = concessionariaRepository.findAll(pageable);
        return concessionarias.map(concessionaria -> modelMapper.map(concessionaria, ConcessionariaDTO.class));
    }

    public ConcessionariaDTO obterPorId(Long id) {
        Concessionaria concessionaria = concessionariaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CONCESSIONARIA_NAO_ENCONTRADA + id));
        return modelMapper.map(concessionaria, ConcessionariaDTO.class);
    }

    public ConcessionariaDTO criarConcessionaria(ConcessionariaCreateDTO concessionariaCreateDTO) {
        if (concessionariaCreateDTO.getNome() == null || concessionariaCreateDTO.getNome().isEmpty()) {
            throw new InvalidRequestException("Nome da concessionária é obrigatório.");
        }

        if (concessionariaCreateDTO.getMarca() == null || concessionariaCreateDTO.getMarca().isEmpty()) {
            throw new InvalidRequestException("Marca da concessionária é obrigatória.");
        }

        Bairro bairro = bairroRepository.findById(concessionariaCreateDTO.getBairroId())
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com ID: " + concessionariaCreateDTO.getBairroId()));

        Concessionaria concessionaria = modelMapper.map(concessionariaCreateDTO, Concessionaria.class);
        concessionaria.setBairro(bairro);
        concessionaria.setTemEstacaoRecarga(concessionariaCreateDTO.getTemEstacaoRecarga() ? 1 : 0);

        Concessionaria concessionariaSalva = concessionariaRepository.save(concessionaria);
        return modelMapper.map(concessionariaSalva, ConcessionariaDTO.class);
    }

    public ConcessionariaDTO atualizarConcessionaria(Long id, ConcessionariaCreateDTO concessionariaCreateDTO) {
        Concessionaria concessionariaExistente = concessionariaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CONCESSIONARIA_NAO_ENCONTRADA + id));

        if (concessionariaCreateDTO.getNome() != null && concessionariaCreateDTO.getNome().isEmpty()) {
            throw new InvalidRequestException("Nome da concessionária não pode ser vazio.");
        }

        if (concessionariaCreateDTO.getMarca() != null && concessionariaCreateDTO.getMarca().isEmpty()) {
            throw new InvalidRequestException("Marca da concessionária não pode ser vazia.");
        }

        concessionariaExistente.setNome(concessionariaCreateDTO.getNome());
        concessionariaExistente.setMarca(concessionariaCreateDTO.getMarca());
        concessionariaExistente.setTemEstacaoRecarga(concessionariaCreateDTO.getTemEstacaoRecarga() ? 1 : 0);

        if (concessionariaCreateDTO.getBairroId() != null && !concessionariaCreateDTO.getBairroId().equals(concessionariaExistente.getBairro().getBairroId())) {
            Bairro novoBairro = bairroRepository.findById(concessionariaCreateDTO.getBairroId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com ID: " + concessionariaCreateDTO.getBairroId()));
            concessionariaExistente.setBairro(novoBairro);
        }

        Concessionaria concessionariaAtualizada = concessionariaRepository.save(concessionariaExistente);
        return modelMapper.map(concessionariaAtualizada, ConcessionariaDTO.class);
    }

    public void deletarConcessionaria(Long id) {
        Concessionaria concessionaria = concessionariaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CONCESSIONARIA_NAO_ENCONTRADA + id));
        concessionariaRepository.delete(concessionaria);
    }

    public List<ConcessionariaDTO> listarPorBairro(Long bairroId) {
        List<Concessionaria> concessionarias = concessionariaRepository.findByBairroBairroId(bairroId);
        if (concessionarias.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma concessionária encontrada para o bairro com ID: " + bairroId);
        }
        return concessionarias.stream()
                .map(concessionaria -> modelMapper.map(concessionaria, ConcessionariaDTO.class))
                .toList();
    }

    public List<ConcessionariaDTO> listarPorMarca(String marca) {
        List<Concessionaria> concessionarias = concessionariaRepository.findByMarcaContainingIgnoreCase(marca);
        if (concessionarias.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma concessionária encontrada para a marca: " + marca);
        }
        return concessionarias.stream()
                .map(concessionaria -> modelMapper.map(concessionaria, ConcessionariaDTO.class))
                .toList();
    }
}