package service;

import dto.Bairro.BairroCreateDTO;
import dto.Bairro.BairroDTO;
import exception.ResourceNotFoundException;
import model.Bairro;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BairroRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BairroService {

    private final BairroRepository bairroRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BairroService(BairroRepository bairroRepository, ModelMapper modelMapper) {
        this.bairroRepository = bairroRepository;
        this.modelMapper = modelMapper;
    }

    public List<BairroDTO> listarTodos() {
        List<Bairro> bairros = bairroRepository.findAll();
        return bairros.stream()
                .map(bairro -> modelMapper.map(bairro, BairroDTO.class))
                .collect(Collectors.toList());
    }

    public BairroDTO obterPorId(Long id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com ID: " + id));
        return modelMapper.map(bairro, BairroDTO.class);
    }

    public BairroDTO criarBairro(BairroCreateDTO bairroCreateDTO) {
        Bairro bairro = modelMapper.map(bairroCreateDTO, Bairro.class);
        Bairro bairroSalvo = bairroRepository.save(bairro);
        return modelMapper.map(bairroSalvo, BairroDTO.class);
    }

    public BairroDTO atualizarBairro(Long id, BairroCreateDTO bairroCreateDTO) {
        Bairro bairroExistente = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com ID: " + id));

        bairroExistente.setNome(bairroCreateDTO.getNome());

        Bairro bairroAtualizado = bairroRepository.save(bairroExistente);
        return modelMapper.map(bairroAtualizado, BairroDTO.class);
    }

    public void deletarBairro(Long id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com ID: " + id));
        bairroRepository.delete(bairro);
    }
}
