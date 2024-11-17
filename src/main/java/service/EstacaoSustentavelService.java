package service;

import dto.EstacaoSustentavel.EstacaoSustentavelCreateDTO;
import dto.EstacaoSustentavel.EstacaoSustentavelDTO;
import exception.ResourceNotFoundException;
import model.EstacaoRecarga;
import model.EstacaoSustentavel;
import model.FonteEnergia;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.EstacaoRecargaRepository;
import repository.EstacaoSustentavelRepository;
import repository.FonteEnergiaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstacaoSustentavelService {

    private final EstacaoSustentavelRepository estacaoSustentavelRepository;
    private final EstacaoRecargaRepository estacaoRecargaRepository;
    private final FonteEnergiaRepository fonteEnergiaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EstacaoSustentavelService(EstacaoSustentavelRepository estacaoSustentavelRepository, EstacaoRecargaRepository estacaoRecargaRepository, FonteEnergiaRepository fonteEnergiaRepository, ModelMapper modelMapper) {
        this.estacaoSustentavelRepository = estacaoSustentavelRepository;
        this.estacaoRecargaRepository = estacaoRecargaRepository;
        this.fonteEnergiaRepository = fonteEnergiaRepository;
        this.modelMapper = modelMapper;
    }

    public List<EstacaoSustentavelDTO> listarTodos() {
        List<EstacaoSustentavel> estacoes = estacaoSustentavelRepository.findAll();
        return estacoes.stream()
                .map(estacao -> modelMapper.map(estacao, EstacaoSustentavelDTO.class))
                .collect(Collectors.toList());
    }

    public EstacaoSustentavelDTO obterPorId(Long id) {
        EstacaoSustentavel estacao = estacaoSustentavelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estação sustentável não encontrada com ID: " + id));
        return modelMapper.map(estacao, EstacaoSustentavelDTO.class);
    }

    public EstacaoSustentavelDTO criarEstacaoSustentavel(EstacaoSustentavelCreateDTO estacaoCreateDTO) {
        EstacaoRecarga estacaoRecarga = estacaoRecargaRepository.findById(estacaoCreateDTO.getEstacaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estação de recarga não encontrada com ID: " + estacaoCreateDTO.getEstacaoId()));

        FonteEnergia fonteEnergia = fonteEnergiaRepository.findById(estacaoCreateDTO.getFonteId())
                .orElseThrow(() -> new ResourceNotFoundException("Fonte de energia não encontrada com ID: " + estacaoCreateDTO.getFonteId()));

        EstacaoSustentavel estacaoSustentavel = modelMapper.map(estacaoCreateDTO, EstacaoSustentavel.class);
        estacaoSustentavel.setEstacaoRecarga(estacaoRecarga);
        estacaoSustentavel.setFonteEnergia(fonteEnergia);

        EstacaoSustentavel estacaoSalva = estacaoSustentavelRepository.save(estacaoSustentavel);
        return modelMapper.map(estacaoSalva, EstacaoSustentavelDTO.class);
    }

    public EstacaoSustentavelDTO atualizarEstacaoSustentavel(Long id, EstacaoSustentavelCreateDTO estacaoCreateDTO) {
        EstacaoSustentavel estacaoExistente = estacaoSustentavelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estação sustentável não encontrada com ID: " + id));

        if (estacaoCreateDTO.getFonteId() != null && !estacaoCreateDTO.getFonteId().equals(estacaoExistente.getFonteEnergia().getFonteId())) {
            FonteEnergia novaFonte = fonteEnergiaRepository.findById(estacaoCreateDTO.getFonteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fonte de energia não encontrada com ID: " + estacaoCreateDTO.getFonteId()));
            estacaoExistente.setFonteEnergia(novaFonte);
        }

        if (estacaoCreateDTO.getReducaoCarbono() != null) {
            estacaoExistente.setReducaoCarbono(estacaoCreateDTO.getReducaoCarbono());
        }

        EstacaoSustentavel estacaoAtualizada = estacaoSustentavelRepository.save(estacaoExistente);
        return modelMapper.map(estacaoAtualizada, EstacaoSustentavelDTO.class);
    }

    public void deletarEstacaoSustentavel(Long id) {
        EstacaoSustentavel estacaoSustentavel = estacaoSustentavelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estação sustentável não encontrada com ID: " + id));
        estacaoSustentavelRepository.delete(estacaoSustentavel);
    }
}
