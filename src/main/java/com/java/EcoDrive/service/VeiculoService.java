package com.java.EcoDrive.service;

import com.java.EcoDrive.dto.Veiculo.VeiculoCreateDTO;
import com.java.EcoDrive.dto.Veiculo.VeiculoDTO;
import com.java.EcoDrive.exception.ResourceNotFoundException;
import com.java.EcoDrive.exception.InvalidRequestException;
import com.java.EcoDrive.model.Usuario;
import com.java.EcoDrive.model.Veiculo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.java.EcoDrive.repository.UsuarioRepository;
import com.java.EcoDrive.repository.VeiculoRepository;

import java.util.List;

@Service
public class VeiculoService {

    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado com ID: ";
    private static final String VEICULO_NAO_ENCONTRADO = "Veículo não encontrado com ID: ";

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
                .toList(); // Substituição para Stream.toList()
    }

    public VeiculoDTO obterPorId(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(VEICULO_NAO_ENCONTRADO + id));
        return modelMapper.map(veiculo, VeiculoDTO.class);
    }

    public List<VeiculoDTO> buscarPorMarca(String marca) {
        List<Veiculo> veiculos = veiculoRepository.buscarVeiculosPorMarca(marca);
        if (veiculos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum veículo encontrado com a marca: " + marca);
        }
        return veiculos.stream()
                .map(veiculo -> modelMapper.map(veiculo, VeiculoDTO.class))
                .toList(); // Substituição para Stream.toList()
    }

    public List<VeiculoDTO> listarVeiculosPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO + usuarioId));

        List<Veiculo> veiculos = veiculoRepository.findByUsuarioNome(usuario.getNome());
        return veiculos.stream()
                .map(veiculo -> modelMapper.map(veiculo, VeiculoDTO.class))
                .toList(); // Substituição para Stream.toList()
    }

    public VeiculoDTO criarVeiculo(VeiculoCreateDTO veiculoCreateDTO) {
        if (veiculoCreateDTO.getMarca() == null || veiculoCreateDTO.getMarca().isEmpty()) {
            throw new InvalidRequestException("A marca do veículo é obrigatória.");
        }
        if (veiculoCreateDTO.getModelo() == null || veiculoCreateDTO.getModelo().isEmpty()) {
            throw new InvalidRequestException("O modelo do veículo é obrigatório.");
        }
        if (veiculoCreateDTO.getAno() == null) {
            throw new InvalidRequestException("O ano do veículo é obrigatório.");
        }
        if (veiculoRepository.existsByMarca(veiculoCreateDTO.getMarca())) {
            throw new InvalidRequestException("A marca já está em uso.");
        }

        Usuario usuario = usuarioRepository.findById(veiculoCreateDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO + veiculoCreateDTO.getUsuarioId()));

        Veiculo veiculo = modelMapper.map(veiculoCreateDTO, Veiculo.class);
        veiculo.setUsuario(usuario);

        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);
        return modelMapper.map(veiculoSalvo, VeiculoDTO.class);
    }

    public VeiculoDTO atualizarVeiculo(Long id, VeiculoCreateDTO veiculoCreateDTO) {
        Veiculo veiculoExistente = veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(VEICULO_NAO_ENCONTRADO + id));

        if (veiculoCreateDTO.getMarca() != null && !veiculoCreateDTO.getMarca().equals(veiculoExistente.getMarca())) {
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

        // Corrigindo a expressão booleana para usar um tipo primitivo
        if (veiculoCreateDTO.getIsEletrico() != null) {
            veiculoExistente.setIsEletrico(veiculoCreateDTO.getIsEletrico() ? 1 : 0);
        }

        if (veiculoCreateDTO.getUsuarioId() != null && !veiculoCreateDTO.getUsuarioId().equals(veiculoExistente.getUsuario().getUsuarioId())) {
            Usuario novoUsuario = usuarioRepository.findById(veiculoCreateDTO.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO + veiculoCreateDTO.getUsuarioId()));
            veiculoExistente.setUsuario(novoUsuario);
        }

        Veiculo veiculoAtualizado = veiculoRepository.save(veiculoExistente);
        return modelMapper.map(veiculoAtualizado, VeiculoDTO.class);
    }

    public void deletarVeiculo(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new ResourceNotFoundException(VEICULO_NAO_ENCONTRADO + id);
        }
        veiculoRepository.deleteById(id);
    }

    public Page<VeiculoDTO> listarTodosPaginado(Pageable pageable) {
        Page<Veiculo> veiculos = veiculoRepository.findAll(pageable);
        return veiculos.map(veiculo -> modelMapper.map(veiculo, VeiculoDTO.class));
    }
}