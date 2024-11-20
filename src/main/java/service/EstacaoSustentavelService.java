package service;

import dto.EstacaoSustentavel.EstacaoSustentavelCreateDTO;
import dto.EstacaoSustentavel.EstacaoSustentavelDTO;
import exception.ResourceNotFoundException;
import exception.InvalidRequestException;
import model.EstacaoRecarga;
import model.EstacaoSustentavel;
import model.FonteEnergia;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.EstacaoRecargaRepository;
import repository.EstacaoSustentavelRepository;
import repository.FonteEnergiaRepository;

import java.util.List;

@Service
public class EstacaoSustentavelService {

    private static final String ESTACAO_SUSTENTAVEL_NAO_ENCONTRADA = "Estação sustentável não encontrada com ID: ";
    private static final String FONTE_NAO_ENCONTRADA = "Fonte de energia não encontrada com ID: ";
    private static final String ESTACAO_RECARGA_NAO_ENCONTRADA = "Estação de recarga não encontrada com ID: ";
    private static final String NENHUMA_ESTACAO_ENCONTRADA = "Nenhuma estação sustentável encontrada para o tipo de energia: ";

    private final EstacaoSustentavelRepository estacaoSustentavelRepository;
    private final EstacaoRecargaRepository estacaoRecargaRepository;
    private final FonteEnergiaRepository fonteEnergiaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EstacaoSustentavelService(EstacaoSustentavelRepository estacaoSustentavelRepository,
                                     EstacaoRecargaRepository estacaoRecargaRepository,
                                     FonteEnergiaRepository fonteEnergiaRepository,
                                     ModelMapper modelMapper) {
        this.estacaoSustentavelRepository = estacaoSustentavelRepository;
        this.estacaoRecargaRepository = estacaoRecargaRepository;
        this.fonteEnergiaRepository = fonteEnergiaRepository;
        this.modelMapper = modelMapper;
    }

    public Page<EstacaoSustentavelDTO> listarTodosPaginado(Pageable pageable) {
        Page<EstacaoSustentavel> estacoes = estacaoSustentavelRepository.findAll(pageable);
        return estacoes.map(estacao -> modelMapper.map(estacao, EstacaoSustentavelDTO.class));
    }

    public EstacaoSustentavelDTO obterPorId(Long id) {
        EstacaoSustentavel estacao = estacaoSustentavelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_SUSTENTAVEL_NAO_ENCONTRADA + id));
        return modelMapper.map(estacao, EstacaoSustentavelDTO.class);
    }

    public EstacaoSustentavelDTO criarEstacaoSustentavel(EstacaoSustentavelCreateDTO estacaoCreateDTO) {
        EstacaoRecarga estacaoRecarga = estacaoRecargaRepository.findById(estacaoCreateDTO.getEstacaoId())
                .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_RECARGA_NAO_ENCONTRADA + estacaoCreateDTO.getEstacaoId()));

        FonteEnergia fonteEnergia = fonteEnergiaRepository.findById(estacaoCreateDTO.getFonteId())
                .orElseThrow(() -> new ResourceNotFoundException(FONTE_NAO_ENCONTRADA + estacaoCreateDTO.getFonteId()));

        if (estacaoCreateDTO.getReducaoCarbono() == null || estacaoCreateDTO.getReducaoCarbono() <= 0) {
            throw new InvalidRequestException("O valor de redução de carbono deve ser maior que zero.");
        }

        EstacaoSustentavel estacaoSustentavel = modelMapper.map(estacaoCreateDTO, EstacaoSustentavel.class);
        estacaoSustentavel.setEstacaoRecarga(estacaoRecarga);
        estacaoSustentavel.setFonteEnergia(fonteEnergia);

        EstacaoSustentavel estacaoSalva = estacaoSustentavelRepository.save(estacaoSustentavel);
        return modelMapper.map(estacaoSalva, EstacaoSustentavelDTO.class);
    }

    public EstacaoSustentavelDTO atualizarEstacaoSustentavel(Long id, EstacaoSustentavelCreateDTO estacaoCreateDTO) {
        EstacaoSustentavel estacaoExistente = estacaoSustentavelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_SUSTENTAVEL_NAO_ENCONTRADA + id));

        if (estacaoCreateDTO.getFonteId() != null && !estacaoCreateDTO.getFonteId().equals(estacaoExistente.getFonteEnergia().getFonteId())) {
            FonteEnergia novaFonte = fonteEnergiaRepository.findById(estacaoCreateDTO.getFonteId())
                    .orElseThrow(() -> new ResourceNotFoundException(FONTE_NAO_ENCONTRADA + estacaoCreateDTO.getFonteId()));
            estacaoExistente.setFonteEnergia(novaFonte);
        }

        if (estacaoCreateDTO.getReducaoCarbono() != null) {
            if (estacaoCreateDTO.getReducaoCarbono() <= 0) {
                throw new InvalidRequestException("O valor de redução de carbono deve ser maior que zero.");
            }
            estacaoExistente.setReducaoCarbono(estacaoCreateDTO.getReducaoCarbono());
        }

        EstacaoSustentavel estacaoAtualizada = estacaoSustentavelRepository.save(estacaoExistente);
        return modelMapper.map(estacaoAtualizada, EstacaoSustentavelDTO.class);
    }

    public void deletarEstacaoSustentavel(Long id) {
        EstacaoSustentavel estacaoSustentavel = estacaoSustentavelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_SUSTENTAVEL_NAO_ENCONTRADA + id));
        estacaoSustentavelRepository.delete(estacaoSustentavel);
    }

    public List<EstacaoSustentavelDTO> listarPorTipoEnergia(String tipoEnergia) {
        List<EstacaoSustentavel> estacoes = estacaoSustentavelRepository.findByFonteEnergiaTipoEnergiaContainingIgnoreCase(tipoEnergia);
        if (estacoes.isEmpty()) {
            throw new ResourceNotFoundException(NENHUMA_ESTACAO_ENCONTRADA + tipoEnergia);
        }
        return estacoes.stream()
                .map(estacao -> modelMapper.map(estacao, EstacaoSustentavelDTO.class))
                .toList(); // Substituído Collectors.toList() por Stream.toList()
    }
}