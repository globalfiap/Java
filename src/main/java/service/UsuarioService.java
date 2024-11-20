package service;

import dto.Usuario.UsuarioCreateDTO;
import dto.Usuario.UsuarioDTO;
import exception.ResourceNotFoundException;
import exception.InvalidRequestException;
import model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado com ID: ";

    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    // Método para listar todos os usuários com paginação
    public Page<UsuarioDTO> listarTodosPaginado(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return usuarios.map(usuario -> modelMapper.map(usuario, UsuarioDTO.class));
    }

    public UsuarioDTO obterPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO + id));
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    public List<UsuarioDTO> buscarPorNome(String nome) {
        List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(nome);
        if (usuarios.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum usuário encontrado com o nome: " + nome);
        }
        return usuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .toList(); // Substituído Collectors.toList() por toList()
    }

    public UsuarioDTO criarUsuario(UsuarioCreateDTO usuarioCreateDTO) {
        // Verificar se o email já está em uso
        if (usuarioCreateDTO.getEmail() == null || usuarioCreateDTO.getEmail().isEmpty()) {
            throw new InvalidRequestException("O email é obrigatório.");
        }
        if (usuarioRepository.existsByEmail(usuarioCreateDTO.getEmail())) {
            throw new InvalidRequestException("O email já está em uso.");
        }

        // Verificar se o nome é fornecido
        if (usuarioCreateDTO.getNome() == null || usuarioCreateDTO.getNome().isEmpty()) {
            throw new InvalidRequestException("O nome é obrigatório.");
        }

        Usuario usuario = modelMapper.map(usuarioCreateDTO, Usuario.class);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return modelMapper.map(usuarioSalvo, UsuarioDTO.class);
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioCreateDTO usuarioCreateDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO + id));

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
            usuarioExistente.setSenha(usuarioCreateDTO.getSenha());
        }
        if (usuarioCreateDTO.getTelefone() != null && !usuarioCreateDTO.getTelefone().isEmpty()) {
            usuarioExistente.setTelefone(usuarioCreateDTO.getTelefone());
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return modelMapper.map(usuarioAtualizado, UsuarioDTO.class);
    }

    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO + id);
        }
        usuarioRepository.deleteById(id);
    }
}