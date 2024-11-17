package service;

import dto.Usuario.UsuarioCreateDTO;
import dto.Usuario.UsuarioDTO;
import exception.ResourceNotFoundException;
import exception.InvalidRequestException;
import model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    public List<UsuarioDTO> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    public UsuarioDTO obterPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    public UsuarioDTO criarUsuario(UsuarioCreateDTO usuarioCreateDTO) {
        // Verificar se o email já está em uso
        if (usuarioCreateDTO.getEmail() == null || usuarioCreateDTO.getEmail().isEmpty()) {
            throw new InvalidRequestException("O email é obrigatório.");
        }
        if (usuarioRepository.existsByEmail(usuarioCreateDTO.getEmail())) {
            throw new InvalidRequestException("O email já está em uso.");
        }

        Usuario usuario = modelMapper.map(usuarioCreateDTO, Usuario.class);
        // Senha não será criptografada aqui
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return modelMapper.map(usuarioSalvo, UsuarioDTO.class);
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioCreateDTO usuarioCreateDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        // Atualizar os campos
        if (usuarioCreateDTO.getNome() != null && !usuarioCreateDTO.getNome().isEmpty()) {
            usuarioExistente.setNome(usuarioCreateDTO.getNome());
        }
        if (usuarioCreateDTO.getEmail() != null && !usuarioCreateDTO.getEmail().isEmpty() && !usuarioCreateDTO.getEmail().equals(usuarioExistente.getEmail())) {
            // Verificar se o novo email já está em uso
            if (usuarioRepository.existsByEmail(usuarioCreateDTO.getEmail())) {
                throw new InvalidRequestException("O email já está em uso.");
            }
            usuarioExistente.setEmail(usuarioCreateDTO.getEmail());
        }
        if (usuarioCreateDTO.getSenha() != null && !usuarioCreateDTO.getSenha().isEmpty()) {
            usuarioExistente.setSenha(usuarioCreateDTO.getSenha()); // Senha não criptografada
        }
        if (usuarioCreateDTO.getTelefone() != null && !usuarioCreateDTO.getTelefone().isEmpty()) {
            usuarioExistente.setTelefone(usuarioCreateDTO.getTelefone());
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return modelMapper.map(usuarioAtualizado, UsuarioDTO.class);
    }

    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
