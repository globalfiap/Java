package service;

import dto.GastoCarregamento.GastoCarregamentoCreateDTO;
import dto.GastoCarregamento.GastoCarregamentoDTO;
import exception.ResourceNotFoundException;
import exception.InvalidRequestException;
import model.GastoCarregamento;
import model.HistoricoCarregamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.GastoCarregamentoRepository;
import repository.HistoricoCarregamentoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GastoCarregamentoService {

    private static final String GASTO_NAO_ENCONTRADO = "Gasto de carregamento não encontrado com ID: ";
    private static final String HISTORICO_NAO_ENCONTRADO = "Histórico de carregamento não encontrado com ID: ";
    private static final String GASTOS_PERIODO_NAO_ENCONTRADOS = "Nenhum gasto de carregamento encontrado entre as datas fornecidas.";

    private final GastoCarregamentoRepository gastoCarregamentoRepository;
    private final HistoricoCarregamentoRepository historicoCarregamentoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public GastoCarregamentoService(GastoCarregamentoRepository gastoCarregamentoRepository,
                                    HistoricoCarregamentoRepository historicoCarregamentoRepository,
                                    ModelMapper modelMapper) {
        this.gastoCarregamentoRepository = gastoCarregamentoRepository;
        this.historicoCarregamentoRepository = historicoCarregamentoRepository;
        this.modelMapper = modelMapper;
    }

    public Page<GastoCarregamentoDTO> listarTodosPaginado(Pageable pageable) {
        Page<GastoCarregamento> gastos = gastoCarregamentoRepository.findAll(pageable);
        return gastos.map(gasto -> modelMapper.map(gasto, GastoCarregamentoDTO.class));
    }

    public List<GastoCarregamentoDTO> listarPorHistoricoCarregamento(Long historicoId) {
        List<GastoCarregamento> gastos = gastoCarregamentoRepository.findByHistoricoCarregamentoHistoricoId(historicoId);
        if (gastos.isEmpty()) {
            throw new ResourceNotFoundException(HISTORICO_NAO_ENCONTRADO + historicoId);
        }
        return gastos.stream()
                .map(gasto -> modelMapper.map(gasto, GastoCarregamentoDTO.class))
                .toList(); // Usando `Stream.toList()` para simplificação e evitar modificações.
    }

    public List<GastoCarregamentoDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<GastoCarregamento> gastos = gastoCarregamentoRepository.findByDataGastoBetween(inicio, fim);
        if (gastos.isEmpty()) {
            throw new ResourceNotFoundException(GASTOS_PERIODO_NAO_ENCONTRADOS);
        }
        return gastos.stream()
                .map(gasto -> modelMapper.map(gasto, GastoCarregamentoDTO.class))
                .toList();
    }

    public GastoCarregamentoDTO obterPorId(Long id) {
        GastoCarregamento gasto = gastoCarregamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GASTO_NAO_ENCONTRADO + id));
        return modelMapper.map(gasto, GastoCarregamentoDTO.class);
    }

    public GastoCarregamentoDTO criarGastoCarregamento(GastoCarregamentoCreateDTO gastoCreateDTO) {
        if (gastoCreateDTO.getHistoricoId() == null) {
            throw new InvalidRequestException("O ID do histórico de carregamento é obrigatório.");
        }

        if (gastoCreateDTO.getCustoTotal() == null || gastoCreateDTO.getCustoTotal() <= 0) {
            throw new InvalidRequestException("O custo total deve ser maior que zero.");
        }

        HistoricoCarregamento historico = historicoCarregamentoRepository.findById(gastoCreateDTO.getHistoricoId())
                .orElseThrow(() -> new ResourceNotFoundException(HISTORICO_NAO_ENCONTRADO + gastoCreateDTO.getHistoricoId()));

        GastoCarregamento gasto = modelMapper.map(gastoCreateDTO, GastoCarregamento.class);
        gasto.setHistoricoCarregamento(historico);

        GastoCarregamento gastoSalvo = gastoCarregamentoRepository.save(gasto);
        return modelMapper.map(gastoSalvo, GastoCarregamentoDTO.class);
    }

    public GastoCarregamentoDTO atualizarGastoCarregamento(Long id, GastoCarregamentoCreateDTO gastoCreateDTO) {
        GastoCarregamento gastoExistente = gastoCarregamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GASTO_NAO_ENCONTRADO + id));

        if (gastoCreateDTO.getCustoTotal() != null) {
            if (gastoCreateDTO.getCustoTotal() <= 0) {
                throw new InvalidRequestException("O custo total deve ser maior que zero.");
            }
            gastoExistente.setCustoTotal(gastoCreateDTO.getCustoTotal());
        }

        GastoCarregamento gastoAtualizado = gastoCarregamentoRepository.save(gastoExistente);
        return modelMapper.map(gastoAtualizado, GastoCarregamentoDTO.class);
    }

    public void deletarGastoCarregamento(Long id) {
        GastoCarregamento gasto = gastoCarregamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GASTO_NAO_ENCONTRADO + id));
        gastoCarregamentoRepository.delete(gasto);
    }
}