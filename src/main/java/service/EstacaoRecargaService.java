package service;

import dto.EstacaoRecarga.EstacaoRecargaCreateDTO;
import dto.EstacaoRecarga.EstacaoRecargaDTO;
import exception.ResourceNotFoundException;
import model.Bairro;
import model.EstacaoRecarga;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BairroRepository;
import repository.EstacaoRecargaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstacaoRecargaService {

    private final EstacaoRecargaRepository estacaoRecargaRepository;
    private final BairroRepository bairroRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EstacaoRecargaService(EstacaoRecargaRepository estacaoRecargaRepository, BairroRepository bairroRepository, ModelMapper modelMapper) {
        this.estacaoRecargaRepository = estacaoRecargaRepository;
        this.bairroRepository = bairroRepository;
        this.modelMapper = modelMapper;
    }

    public List<EstacaoRecargaDTO> listarTodos() {
        List<EstacaoRecarga> estacoes = estacaoRecargaRepository.findAll();
        return estacoes.stream()
                .map(estacao -> modelMapper.map(estacao, EstacaoRecargaDTO.class))
                .collect(Collectors.toList());
    }

    public EstacaoRecargaDTO obterPorId(Long id) {
        EstacaoRecarga estacao = estacaoRecargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estação de recarga não encontrada com ID: " + id));
        return modelMapper.map(estacao, EstacaoRecargaDTO.class);
    }

    public EstacaoRecargaDTO criarEstacaoRecarga(EstacaoRecargaCreateDTO estacaoCreateDTO) {
        Bairro bairro = bairroRepository.findById(estacaoCreateDTO.getBairroId())
                .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com ID: " + estacaoCreateDTO.getBairroId()));

        EstacaoRecarga estacaoRecarga = modelMapper.map(estacaoCreateDTO, EstacaoRecarga.class);
        estacaoRecarga.setBairro(bairro);

        EstacaoRecarga estacaoSalva = estacaoRecargaRepository.save(estacaoRecarga);
        return modelMapper.map(estacaoSalva, EstacaoRecargaDTO.class);
    }

    public EstacaoRecargaDTO atualizarEstacaoRecarga(Long id, EstacaoRecargaCreateDTO estacaoCreateDTO) {
        EstacaoRecarga estacaoExistente = estacaoRecargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estação de recarga não encontrada com ID: " + id));

        estacaoExistente.setNome(estacaoCreateDTO.getNome());
        estacaoExistente.setLatitude(estacaoCreateDTO.getLatitude());
        estacaoExistente.setLongitude(estacaoCreateDTO.getLongitude());
        estacaoExistente.setTipoCarregador(estacaoCreateDTO.getTipoCarregador());
        estacaoExistente.setPrecoPorKwh(estacaoCreateDTO.getPrecoPorKwh());

        if (estacaoCreateDTO.getBairroId() != null && !estacaoCreateDTO.getBairroId().equals(estacaoExistente.getBairro().getBairroId())) {
            Bairro novoBairro = bairroRepository.findById(estacaoCreateDTO.getBairroId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bairro não encontrado com ID: " + estacaoCreateDTO.getBairroId()));
            estacaoExistente.setBairro(novoBairro);
        }

        EstacaoRecarga estacaoAtualizada = estacaoRecargaRepository.save(estacaoExistente);
        return modelMapper.map(estacaoAtualizada, EstacaoRecargaDTO.class);
    }

    public void deletarEstacaoRecarga(Long id) {
        EstacaoRecarga estacaoRecarga = estacaoRecargaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estação de recarga não encontrada com ID: " + id));
        estacaoRecargaRepository.delete(estacaoRecarga);
    }
}
