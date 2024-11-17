package service;

import dto.GastoCarregamento.GastoCarregamentoCreateDTO;
import dto.GastoCarregamento.GastoCarregamentoDTO;
import exception.ResourceNotFoundException;
import exception.InvalidRequestException;
import model.GastoCarregamento;
import model.HistoricoCarregamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.GastoCarregamentoRepository;
import repository.HistoricoCarregamentoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GastoCarregamentoService {

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

    public List<GastoCarregamentoDTO> listarTodos() {
        List<GastoCarregamento> gastos = gastoCarregamentoRepository.findAll();
        return gastos.stream()
                .map(gasto -> modelMapper.map(gasto, GastoCarregamentoDTO.class))
                .collect(Collectors.toList());
    }

    public List<GastoCarregamentoDTO> listarPorHistoricoCarregamento(Long historicoId) {
        List<GastoCarregamento> gastos = gastoCarregamentoRepository.findByHistoricoCarregamentoHistoricoId(historicoId);
        if (gastos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum gasto de carregamento encontrado para o histórico com ID: " + historicoId);
        }
        return gastos.stream()
                .map(gasto -> modelMapper.map(gasto, GastoCarregamentoDTO.class))
                .collect(Collectors.toList());
    }

    public List<GastoCarregamentoDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<GastoCarregamento> gastos = gastoCarregamentoRepository.findByDataGastoBetween(inicio, fim);
        if (gastos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum gasto de carregamento encontrado entre as datas fornecidas.");
        }
        return gastos.stream()
                .map(gasto -> modelMapper.map(gasto, GastoCarregamentoDTO.class))
                .collect(Collectors.toList());
    }

    public GastoCarregamentoDTO obterPorId(Long id) {
        GastoCarregamento gasto = gastoCarregamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto de carregamento não encontrado com ID: " + id));
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
                .orElseThrow(() -> new ResourceNotFoundException("Histórico de carregamento não encontrado com ID: " + gastoCreateDTO.getHistoricoId()));

        GastoCarregamento gasto = modelMapper.map(gastoCreateDTO, GastoCarregamento.class);
        gasto.setHistoricoCarregamento(historico);

        GastoCarregamento gastoSalvo = gastoCarregamentoRepository.save(gasto);
        return modelMapper.map(gastoSalvo, GastoCarregamentoDTO.class);
    }

    public GastoCarregamentoDTO atualizarGastoCarregamento(Long id, GastoCarregamentoCreateDTO gastoCreateDTO) {
        GastoCarregamento gastoExistente = gastoCarregamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto de carregamento não encontrado com ID: " + id));

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
                .orElseThrow(() -> new ResourceNotFoundException("Gasto de carregamento não encontrado com ID: " + id));
        gastoCarregamentoRepository.delete(gasto);
    }
}
