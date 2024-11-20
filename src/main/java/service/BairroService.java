package service;

import dto.Bairro.BairroCreateDTO;
import dto.Bairro.BairroDTO;
import exception.ResourceNotFoundException;
import exception.InvalidRequestException;
import model.Bairro;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.BairroRepository;

@Service
public class BairroService {

    private static final String BAIRRO_NAO_ENCONTRADO = "Bairro não encontrado com ID: ";

    private final BairroRepository bairroRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BairroService(BairroRepository bairroRepository, ModelMapper modelMapper) {
        this.bairroRepository = bairroRepository;
        this.modelMapper = modelMapper;
    }

    public Page<BairroDTO> listarTodosPaginado(Pageable pageable) {
        Page<Bairro> bairros = bairroRepository.findAll(pageable);
        return bairros.map(bairro -> modelMapper.map(bairro, BairroDTO.class));
    }

    public BairroDTO obterPorId(Long id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BAIRRO_NAO_ENCONTRADO + id));
        return modelMapper.map(bairro, BairroDTO.class);
    }

    public BairroDTO criarBairro(BairroCreateDTO bairroCreateDTO) {
        if (bairroCreateDTO.getNome() == null || bairroCreateDTO.getNome().isEmpty()) {
            throw new InvalidRequestException("Nome do bairro é obrigatório.");
        }

        Bairro bairro = modelMapper.map(bairroCreateDTO, Bairro.class);
        Bairro bairroSalvo = bairroRepository.save(bairro);
        return modelMapper.map(bairroSalvo, BairroDTO.class);
    }

    public BairroDTO atualizarBairro(Long id, BairroCreateDTO bairroCreateDTO) {
        Bairro bairroExistente = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BAIRRO_NAO_ENCONTRADO + id));

        if (bairroCreateDTO.getNome() == null || bairroCreateDTO.getNome().isEmpty()) {
            throw new InvalidRequestException("Nome do bairro não pode ser vazio.");
        }

        bairroExistente.setNome(bairroCreateDTO.getNome());

        Bairro bairroAtualizado = bairroRepository.save(bairroExistente);
        return modelMapper.map(bairroAtualizado, BairroDTO.class);
    }

    public void deletarBairro(Long id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BAIRRO_NAO_ENCONTRADO + id));
        bairroRepository.delete(bairro);
    }
}