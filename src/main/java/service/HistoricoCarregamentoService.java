package service;

import dto.HistoricoCarregamento.HistoricoCarregamentoCreateDTO;
import dto.HistoricoCarregamento.HistoricoCarregamentoDTO;
import exception.ResourceNotFoundException;
import exception.InvalidRequestException;
import model.HistoricoCarregamento;
import model.Usuario;
import model.Veiculo;
import model.EstacaoRecarga;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import repository.HistoricoCarregamentoRepository;
import repository.UsuarioRepository;
import repository.VeiculoRepository;
import repository.EstacaoRecargaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoCarregamentoService {

    private final HistoricoCarregamentoRepository historicoCarregamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VeiculoRepository veiculoRepository;
    private final EstacaoRecargaRepository estacaoRecargaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public HistoricoCarregamentoService(HistoricoCarregamentoRepository historicoCarregamentoRepository,
                                        UsuarioRepository usuarioRepository,
                                        VeiculoRepository veiculoRepository,
                                        EstacaoRecargaRepository estacaoRecargaRepository,
                                        ModelMapper modelMapper) {
        this.historicoCarregamentoRepository = historicoCarregamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.veiculoRepository = veiculoRepository;
        this.estacaoRecargaRepository = estacaoRecargaRepository;
        this.modelMapper = modelMapper;
    }

    public Page<HistoricoCarregamentoDTO> listarTodosPaginado(Pageable pageable) {
        Page<HistoricoCarregamento> historicos = historicoCarregamentoRepository.findAll(pageable);
        return historicos.map(historico -> modelMapper.map(historico, HistoricoCarregamentoDTO.class));
    }

    public List<HistoricoCarregamentoDTO> listarTodos() {
        List<HistoricoCarregamento> historicos = historicoCarregamentoRepository.findAll();
        return historicos.stream()
                .map(historico -> modelMapper.map(historico, HistoricoCarregamentoDTO.class))
                .collect(Collectors.toList());
    }

    public HistoricoCarregamentoDTO obterPorId(Long id) {
        HistoricoCarregamento historico = historicoCarregamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico de carregamento não encontrado com ID: " + id));
        return modelMapper.map(historico, HistoricoCarregamentoDTO.class);
    }

    public List<HistoricoCarregamentoDTO> listarPorUsuario(Long usuarioId) {
        List<HistoricoCarregamento> historicos = historicoCarregamentoRepository.findByUsuarioUsuarioId(usuarioId);
        if (historicos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum histórico de carregamento encontrado para o usuário com ID: " + usuarioId);
        }
        return historicos.stream()
                .map(historico -> modelMapper.map(historico, HistoricoCarregamentoDTO.class))
                .collect(Collectors.toList());
    }

    public List<HistoricoCarregamentoDTO> listarPorVeiculo(Long veiculoId) {
        List<HistoricoCarregamento> historicos = historicoCarregamentoRepository.findByVeiculoVeiculoId(veiculoId);
        if (historicos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum histórico de carregamento encontrado para o veículo com ID: " + veiculoId);
        }
        return historicos.stream()
                .map(historico -> modelMapper.map(historico, HistoricoCarregamentoDTO.class))
                .collect(Collectors.toList());
    }

    public HistoricoCarregamentoDTO criarHistorico(HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        if (historicoCreateDTO.getUsuarioId() == null || historicoCreateDTO.getVeiculoId() == null || historicoCreateDTO.getEstacaoId() == null) {
            throw new InvalidRequestException("ID de usuário, veículo e estação de recarga são obrigatórios.");
        }

        if (historicoCreateDTO.getKwhConsumidos() == null || historicoCreateDTO.getKwhConsumidos() <= 0) {
            throw new InvalidRequestException("Kwh consumidos deve ser maior que zero.");
        }

        Usuario usuario = usuarioRepository.findById(historicoCreateDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + historicoCreateDTO.getUsuarioId()));
        Veiculo veiculo = veiculoRepository.findById(historicoCreateDTO.getVeiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com ID: " + historicoCreateDTO.getVeiculoId()));
        EstacaoRecarga estacaoRecarga = estacaoRecargaRepository.findById(historicoCreateDTO.getEstacaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Estação de recarga não encontrada com ID: " + historicoCreateDTO.getEstacaoId()));

        HistoricoCarregamento historico = modelMapper.map(historicoCreateDTO, HistoricoCarregamento.class);
        historico.setUsuario(usuario);
        historico.setVeiculo(veiculo);
        historico.setEstacaoRecarga(estacaoRecarga);

        HistoricoCarregamento historicoSalvo = historicoCarregamentoRepository.save(historico);
        return modelMapper.map(historicoSalvo, HistoricoCarregamentoDTO.class);
    }

    public HistoricoCarregamentoDTO atualizarHistorico(Long id, HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        HistoricoCarregamento historicoExistente = historicoCarregamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico de carregamento não encontrado com ID: " + id));

        if (historicoCreateDTO.getKwhConsumidos() != null) {
            if (historicoCreateDTO.getKwhConsumidos() <= 0) {
                throw new InvalidRequestException("Kwh consumidos deve ser maior que zero.");
            }
            historicoExistente.setKwhConsumidos(historicoCreateDTO.getKwhConsumidos());
        }

        if (historicoCreateDTO.getDataCarregamento() != null) {
            historicoExistente.setDataCarregamento(historicoCreateDTO.getDataCarregamento());
        }

        HistoricoCarregamento historicoAtualizado = historicoCarregamentoRepository.save(historicoExistente);
        return modelMapper.map(historicoAtualizado, HistoricoCarregamentoDTO.class);
    }

    public void deletarHistorico(Long id) {
        HistoricoCarregamento historico = historicoCarregamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico de carregamento não encontrado com ID: " + id));
        historicoCarregamentoRepository.delete(historico);
    }
}
