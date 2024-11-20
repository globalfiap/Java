package service;

import dto.FonteEnergia.FonteEnergiaCreateDTO;
import dto.FonteEnergia.FonteEnergiaDTO;
import exception.ResourceNotFoundException;
import exception.InvalidRequestException;
import model.FonteEnergia;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.FonteEnergiaRepository;

import java.util.List;

@Service
public class FonteEnergiaService {

    private static final String FONTE_NAO_ENCONTRADA = "Fonte de energia não encontrada com ID: ";

    private final FonteEnergiaRepository fonteEnergiaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public FonteEnergiaService(FonteEnergiaRepository fonteEnergiaRepository, ModelMapper modelMapper) {
        this.fonteEnergiaRepository = fonteEnergiaRepository;
        this.modelMapper = modelMapper;
    }

    public Page<FonteEnergiaDTO> listarTodasPaginado(Pageable pageable) {
        Page<FonteEnergia> fontes = fonteEnergiaRepository.findAll(pageable);
        return fontes.map(fonte -> modelMapper.map(fonte, FonteEnergiaDTO.class));
    }

    public FonteEnergiaDTO obterPorId(Long id) {
        FonteEnergia fonte = fonteEnergiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(FONTE_NAO_ENCONTRADA + id));
        return modelMapper.map(fonte, FonteEnergiaDTO.class);
    }

    public FonteEnergiaDTO criarFonteEnergia(FonteEnergiaCreateDTO fonteCreateDTO) {
        validarFonteEnergia(fonteCreateDTO);
        FonteEnergia fonte = modelMapper.map(fonteCreateDTO, FonteEnergia.class);
        FonteEnergia fonteSalva = fonteEnergiaRepository.save(fonte);
        return modelMapper.map(fonteSalva, FonteEnergiaDTO.class);
    }

    public FonteEnergiaDTO atualizarFonteEnergia(Long id, FonteEnergiaCreateDTO fonteCreateDTO) {
        FonteEnergia fonteExistente = fonteEnergiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(FONTE_NAO_ENCONTRADA + id));
        validarFonteEnergia(fonteCreateDTO);
        fonteExistente.setTipoEnergia(fonteCreateDTO.getTipoEnergia());

        FonteEnergia fonteAtualizada = fonteEnergiaRepository.save(fonteExistente);
        return modelMapper.map(fonteAtualizada, FonteEnergiaDTO.class);
    }

    public void deletarFonteEnergia(Long id) {
        FonteEnergia fonte = fonteEnergiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(FONTE_NAO_ENCONTRADA + id));
        fonteEnergiaRepository.delete(fonte);
    }

    public List<FonteEnergiaDTO> listarPorTipoEnergia(String tipoEnergia) {
        List<FonteEnergia> fontes = fonteEnergiaRepository.findByTipoEnergiaContainingIgnoreCase(tipoEnergia);
        if (fontes.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma fonte de energia encontrada contendo: " + tipoEnergia);
        }
        return fontes.stream()
                .map(fonte -> modelMapper.map(fonte, FonteEnergiaDTO.class))
                .toList(); // Substituído Collectors.toList() por Stream.toList()
    }

    private void validarFonteEnergia(FonteEnergiaCreateDTO fonteCreateDTO) {
        if (fonteCreateDTO.getTipoEnergia() == null || fonteCreateDTO.getTipoEnergia().isEmpty()) {
            throw new InvalidRequestException("O tipo de energia é obrigatório e não pode ser vazio.");
        }
    }
}