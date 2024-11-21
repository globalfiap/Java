package com.java.EcoDrive.service;

import com.java.EcoDrive.dto.HistoricoCarregamento.HistoricoCarregamentoCreateDTO;
import com.java.EcoDrive.dto.HistoricoCarregamento.HistoricoCarregamentoDTO;
import com.java.EcoDrive.exception.ResourceNotFoundException;
import com.java.EcoDrive.exception.InvalidRequestException;
import com.java.EcoDrive.model.HistoricoCarregamento;
import com.java.EcoDrive.model.Usuario;
import com.java.EcoDrive.model.Veiculo;
import com.java.EcoDrive.model.EstacaoRecarga;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.java.EcoDrive.repository.HistoricoCarregamentoRepository;
import com.java.EcoDrive.repository.UsuarioRepository;
import com.java.EcoDrive.repository.VeiculoRepository;
import com.java.EcoDrive.repository.EstacaoRecargaRepository;

import java.util.List;

@Service
public class HistoricoCarregamentoService {

    private static final String HISTORICO_NAO_ENCONTRADO = "Histórico de carregamento não encontrado com ID: ";
    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado com ID: ";
    private static final String VEICULO_NAO_ENCONTRADO = "Veículo não encontrado com ID: ";
    private static final String ESTACAO_NAO_ENCONTRADA = "Estação de recarga não encontrada com ID: ";

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
        return historicoCarregamentoRepository.findAll(pageable)
                .map(historico -> modelMapper.map(historico, HistoricoCarregamentoDTO.class));
    }

    public List<HistoricoCarregamentoDTO> listarTodos() {
        return historicoCarregamentoRepository.findAll().stream()
                .map(historico -> modelMapper.map(historico, HistoricoCarregamentoDTO.class))
                .toList();
    }

    public HistoricoCarregamentoDTO obterPorId(Long id) {
        HistoricoCarregamento historico = historicoCarregamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HISTORICO_NAO_ENCONTRADO + id));
        return modelMapper.map(historico, HistoricoCarregamentoDTO.class);
    }

    public List<HistoricoCarregamentoDTO> listarPorUsuario(Long usuarioId) {
        return historicoCarregamentoRepository.findByUsuarioUsuarioId(usuarioId).stream()
                .map(historico -> modelMapper.map(historico, HistoricoCarregamentoDTO.class))
                .toList();
    }

    public List<HistoricoCarregamentoDTO> listarPorVeiculo(Long veiculoId) {
        return historicoCarregamentoRepository.findByVeiculoVeiculoId(veiculoId).stream()
                .map(historico -> modelMapper.map(historico, HistoricoCarregamentoDTO.class))
                .toList();
    }

    public HistoricoCarregamentoDTO criarHistorico(HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        if (historicoCreateDTO.getUsuarioId() == null || historicoCreateDTO.getVeiculoId() == null || historicoCreateDTO.getEstacaoId() == null) {
            throw new InvalidRequestException("ID de usuário, veículo e estação de recarga são obrigatórios.");
        }

        if (historicoCreateDTO.getKwhConsumidos() == null || historicoCreateDTO.getKwhConsumidos() <= 0) {
            throw new InvalidRequestException("Kwh consumidos deve ser maior que zero.");
        }

        Usuario usuario = usuarioRepository.findById(historicoCreateDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO + historicoCreateDTO.getUsuarioId()));
        Veiculo veiculo = veiculoRepository.findById(historicoCreateDTO.getVeiculoId())
                .orElseThrow(() -> new ResourceNotFoundException(VEICULO_NAO_ENCONTRADO + historicoCreateDTO.getVeiculoId()));
        EstacaoRecarga estacaoRecarga = estacaoRecargaRepository.findById(historicoCreateDTO.getEstacaoId())
                .orElseThrow(() -> new ResourceNotFoundException(ESTACAO_NAO_ENCONTRADA + historicoCreateDTO.getEstacaoId()));

        HistoricoCarregamento historico = modelMapper.map(historicoCreateDTO, HistoricoCarregamento.class);
        historico.setUsuario(usuario);
        historico.setVeiculo(veiculo);
        historico.setEstacaoRecarga(estacaoRecarga);

        HistoricoCarregamento historicoSalvo = historicoCarregamentoRepository.save(historico);
        return modelMapper.map(historicoSalvo, HistoricoCarregamentoDTO.class);
    }

    public HistoricoCarregamentoDTO atualizarHistorico(Long id, HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        HistoricoCarregamento historicoExistente = historicoCarregamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HISTORICO_NAO_ENCONTRADO + id));

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
                .orElseThrow(() -> new ResourceNotFoundException(HISTORICO_NAO_ENCONTRADO + id));
        historicoCarregamentoRepository.delete(historico);
    }
}