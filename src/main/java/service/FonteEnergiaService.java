package service;

import dto.FonteEnergia.FonteEnergiaCreateDTO;
import dto.FonteEnergia.FonteEnergiaDTO;
import exception.ResourceNotFoundException;
import model.FonteEnergia;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.FonteEnergiaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FonteEnergiaService {

    private final FonteEnergiaRepository fonteEnergiaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public FonteEnergiaService(FonteEnergiaRepository fonteEnergiaRepository, ModelMapper modelMapper) {
        this.fonteEnergiaRepository = fonteEnergiaRepository;
        this.modelMapper = modelMapper;
    }

    public List<FonteEnergiaDTO> listarTodas() {
        List<FonteEnergia> fontes = fonteEnergiaRepository.findAll();
        return fontes.stream()
                .map(fonte -> modelMapper.map(fonte, FonteEnergiaDTO.class))
                .collect(Collectors.toList());
    }

    public FonteEnergiaDTO obterPorId(Long id) {
        FonteEnergia fonte = fonteEnergiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fonte de energia não encontrada com ID: " + id));
        return modelMapper.map(fonte, FonteEnergiaDTO.class);
    }

    public FonteEnergiaDTO criarFonteEnergia(FonteEnergiaCreateDTO fonteCreateDTO) {
        FonteEnergia fonte = modelMapper.map(fonteCreateDTO, FonteEnergia.class);
        FonteEnergia fonteSalva = fonteEnergiaRepository.save(fonte);
        return modelMapper.map(fonteSalva, FonteEnergiaDTO.class);
    }

    public FonteEnergiaDTO atualizarFonteEnergia(Long id, FonteEnergiaCreateDTO fonteCreateDTO) {
        FonteEnergia fonteExistente = fonteEnergiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fonte de energia não encontrada com ID: " + id));

        if (fonteCreateDTO.getTipoEnergia() != null) {
            fonteExistente.setTipoEnergia(fonteCreateDTO.getTipoEnergia());
        }

        FonteEnergia fonteAtualizada = fonteEnergiaRepository.save(fonteExistente);
        return modelMapper.map(fonteAtualizada, FonteEnergiaDTO.class);
    }

    public void deletarFonteEnergia(Long id) {
        FonteEnergia fonte = fonteEnergiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fonte de energia não encontrada com ID: " + id));
        fonteEnergiaRepository.delete(fonte);
    }
}