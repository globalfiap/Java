package service;

import dto.Concessionaria.ConcessionariaCreateDTO;
import dto.Concessionaria.ConcessionariaDTO;
import exception.ResourceNotFoundException;
import exception.InvalidRequestException;
import model.Bairro;
import model.Concessionaria;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BairroRepository;
import repository.ConcessionariaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConcessionariaService {

    private final ConcessionariaRepository concessionariaRepository;
    private final BairroRepository bairroRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ConcessionariaService(ConcessionariaRepository concessionariaRepository, BairroRepository bairroRepository, ModelMapper modelMapper) {
        this.concessionariaRepository = concessionariaRepository;
        this.bairroRepository = bairroRepository;
        this.modelMapper = modelMapper;
    }

    public List<ConcessionariaDTO> listarTodos() {
        List<Concessionaria> concessionarias = concessionariaRepository.findAll();
        return concessionarias.stream()
                .map(concessionaria -> modelMapper.map(concessionaria, ConcessionariaDTO.class))
                .collect(Collectors.toList());
    }

    public ConcessionariaDTO obterPorId(Long id) {
        Concessionaria concessionaria = concessionariaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concessionária não encontrada com ID: " + id));
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

        Concessionaria concessionariaSalva = concessionariaRepository.save(concessionaria);
        return modelMapper.map(concessionariaSalva, ConcessionariaDTO.class);
    }

    public ConcessionariaDTO atualizarConcessionaria(Long id, ConcessionariaCreateDTO concessionariaCreateDTO) {
        Concessionaria concessionariaExistente = concessionariaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concessionária não encontrada com ID: " + id));

        if (concessionariaCreateDTO.getNome() != null && concessionariaCreateDTO.getNome().isEmpty()) {
            throw new InvalidRequestException("Nome da concessionária não pode ser vazio.");
        }

        if (concessionariaCreateDTO.getMarca() != null && concessionariaCreateDTO.getMarca().isEmpty()) {
            throw new InvalidRequestException("Marca da concessionária não pode ser vazia.");
        }

        concessionariaExistente.setNome(concessionariaCreateDTO.getNome());
        concessionariaExistente.setMarca(concessionariaCreateDTO.getMarca());
        concessionariaExistente.setTemEstacaoRecarga(concessionariaCreateDTO.getTemEstacaoRecarga());

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
                .orElseThrow(() -> new ResourceNotFoundException("Concessionária não encontrada com ID: " + id));
        concessionariaRepository.delete(concessionaria);
    }
}
