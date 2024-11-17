package service;

import dto.Veiculo.VeiculoCreateDTO;
import dto.Veiculo.VeiculoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import exception.ResourceNotFoundException;
import exception.InvalidRequestException;
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

    public List<VeiculoDTO> buscarPorMarca(String marca) {
        List<Veiculo> veiculos = veiculoRepository.buscarVeiculosPorMarca(marca);
        if (veiculos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum veículo encontrado com a marca: " + marca);
        }
        return veiculos.stream()
                .map(veiculo -> modelMapper.map(veiculo, VeiculoDTO.class))
                .collect(Collectors.toList());
    }

    public List<VeiculoDTO> listarVeiculosPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        List<Veiculo> veiculos = veiculoRepository.findByUsuarioNome(usuario.getNome());
        return veiculos.stream()
                .map(veiculo -> modelMapper.map(veiculo, VeiculoDTO.class))
                .collect(Collectors.toList());
    }

    public VeiculoDTO criarVeiculo(VeiculoCreateDTO veiculoCreateDTO) {
        // Verificar se os campos obrigatórios estão preenchidos
        if (veiculoCreateDTO.getMarca() == null || veiculoCreateDTO.getMarca().isEmpty()) {
            throw new InvalidRequestException("A marca do veículo é obrigatória.");
        }
        if (veiculoCreateDTO.getModelo() == null || veiculoCreateDTO.getModelo().isEmpty()) {
            throw new InvalidRequestException("O modelo do veículo é obrigatório.");
        }
        if (veiculoCreateDTO.getAno() == null) {
            throw new InvalidRequestException("O ano do veículo é obrigatório.");
        }

        // Verificar se a marca já está em uso
        if (veiculoRepository.existsByMarca(veiculoCreateDTO.getMarca())) {
            throw new InvalidRequestException("A marca já está em uso.");
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
                throw new InvalidRequestException("A marca já está em uso.");
            }
            veiculoExistente.setMarca(veiculoCreateDTO.getMarca());
        }

        if (veiculoCreateDTO.getModelo() != null && !veiculoCreateDTO.getModelo().isEmpty()) {
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

    public Page<VeiculoDTO> listarTodosPaginado(Pageable pageable) {
        Page<Veiculo> veiculos = veiculoRepository.findAll(pageable);
        return veiculos.map(veiculo -> modelMapper.map(veiculo, VeiculoDTO.class));
    }
}
