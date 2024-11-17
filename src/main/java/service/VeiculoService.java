package service;

import dto.Veiculo.VeiculoCreateDTO;
import dto.Veiculo.VeiculoDTO;
import exception.ResourceNotFoundException;
import model.Usuario;
import model.Veiculo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.UsuarioRepository;
import repository.VeiculoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public VeiculoService(VeiculoRepository veiculoRepository, UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
        this.veiculoRepository = veiculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    public List<VeiculoDTO> listarTodos() {
        List<Veiculo> veiculos = veiculoRepository.findAll();
        return veiculos.stream()
                .map(veiculo -> modelMapper.map(veiculo, VeiculoDTO.class))
                .collect(Collectors.toList());
    }

    public VeiculoDTO obterPorId(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com ID: " + id));
        return modelMapper.map(veiculo, VeiculoDTO.class);
    }

    public VeiculoDTO criarVeiculo(VeiculoCreateDTO veiculoCreateDTO) {
        // Verificar se a marca já está em uso
        if (veiculoRepository.existsByMarca(veiculoCreateDTO.getMarca())) {
            throw new IllegalArgumentException("A marca já está em uso.");
        }

        // Obter o usuário associado
        Usuario usuario = usuarioRepository.findById(veiculoCreateDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + veiculoCreateDTO.getUsuarioId()));

        Veiculo veiculo = modelMapper.map(veiculoCreateDTO, Veiculo.class);
        veiculo.setUsuario(usuario);

        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);
        return modelMapper.map(veiculoSalvo, VeiculoDTO.class);
    }

    public VeiculoDTO atualizarVeiculo(Long id, VeiculoCreateDTO veiculoCreateDTO) {
        Veiculo veiculoExistente = veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com ID: " + id));

        // Atualizar os campos
        if (veiculoCreateDTO.getMarca() != null && !veiculoCreateDTO.getMarca().equals(veiculoExistente.getMarca())) {
            // Verificar se a nova marca já está em uso
            if (veiculoRepository.existsByMarca(veiculoCreateDTO.getMarca())) {
                throw new IllegalArgumentException("A marca já está em uso.");
            }
            veiculoExistente.setMarca(veiculoCreateDTO.getMarca());
        }

        if (veiculoCreateDTO.getModelo() != null) {
            veiculoExistente.setModelo(veiculoCreateDTO.getModelo());
        }

        if (veiculoCreateDTO.getAno() != null) {
            veiculoExistente.setAno(veiculoCreateDTO.getAno());
        }

        if (veiculoCreateDTO.getIsEletrico() != null) {
            veiculoExistente.setIsEletrico(veiculoCreateDTO.getIsEletrico() ? 1 : 0);
        }

        if (veiculoCreateDTO.getUsuarioId() != null && !veiculoCreateDTO.getUsuarioId().equals(veiculoExistente.getUsuario().getUsuarioId())) {
            // Obter o novo usuário associado
            Usuario novoUsuario = usuarioRepository.findById(veiculoCreateDTO.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + veiculoCreateDTO.getUsuarioId()));
            veiculoExistente.setUsuario(novoUsuario);
        }

        Veiculo veiculoAtualizado = veiculoRepository.save(veiculoExistente);
        return modelMapper.map(veiculoAtualizado, VeiculoDTO.class);
    }

    public void deletarVeiculo(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veículo não encontrado com ID: " + id);
        }
        veiculoRepository.deleteById(id);
    }
}
