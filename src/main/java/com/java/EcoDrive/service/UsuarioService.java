package com.java.EcoDrive.service;

import com.java.EcoDrive.dto.Usuario.UsuarioCreateDTO;
import com.java.EcoDrive.dto.Usuario.UsuarioDTO;
import com.java.EcoDrive.exception.ResourceNotFoundException;
import com.java.EcoDrive.exception.InvalidRequestException;
import com.java.EcoDrive.model.Usuario;
import com.java.EcoDrive.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado com ID: ";
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    public Page<UsuarioDTO> listarTodosPaginado(Pageable pageable) {
        logger.info("Iniciando a listagem de usuários. Página: {}, Tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);

        if (usuarios.isEmpty()) {
            logger.warn("Nenhum usuário encontrado na página solicitada.");
            throw new ResourceNotFoundException("Nenhum usuário encontrado.");
        }

        try {
            return usuarios.map(usuario -> modelMapper.map(usuario, UsuarioDTO.class));
        } catch (Exception e) {
            logger.error("Erro ao mapear os usuários para DTO: {}", e.getMessage(), e);
            throw new RuntimeException("Erro interno ao processar a lista de usuários.");
        }
    }

    public UsuarioDTO obterPorId(Long id) {
        logger.info("Buscando usuário pelo ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado com ID: {}", id);
                    return new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO + id);
                });

        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    public List<UsuarioDTO> buscarPorNome(String nome) {
        logger.info("Buscando usuários pelo nome: {}", nome);

        List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(nome);
        if (usuarios.isEmpty()) {
            logger.warn("Nenhum usuário encontrado com o nome: {}", nome);
            throw new ResourceNotFoundException("Nenhum usuário encontrado com o nome: " + nome);
        }

        return usuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                .toList();
    }

    public UsuarioDTO criarUsuario(UsuarioCreateDTO usuarioCreateDTO) {
        logger.info("Criando um novo usuário com email: {}", usuarioCreateDTO.getEmail());

        validarCamposObrigatorios(usuarioCreateDTO);

        if (usuarioRepository.existsByEmail(usuarioCreateDTO.getEmail())) {
            logger.warn("O email já está em uso: {}", usuarioCreateDTO.getEmail());
            throw new InvalidRequestException("O email já está em uso.");
        }

        Usuario usuario = modelMapper.map(usuarioCreateDTO, Usuario.class);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        logger.info("Usuário criado com sucesso com ID: {}", usuarioSalvo.getUsuarioId());
        return modelMapper.map(usuarioSalvo, UsuarioDTO.class);
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioCreateDTO usuarioCreateDTO) {
        logger.info("Atualizando o usuário com ID: {}", id);

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado com ID: {}", id);
                    return new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO + id);
                });

        atualizarCampos(usuarioExistente, usuarioCreateDTO);

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);

        logger.info("Usuário com ID: {} atualizado com sucesso.", id);
        return modelMapper.map(usuarioAtualizado, UsuarioDTO.class);
    }

    private void validarCamposObrigatorios(UsuarioCreateDTO usuarioCreateDTO) {
        if (usuarioCreateDTO.getEmail() == null || usuarioCreateDTO.getEmail().isEmpty()) {
            logger.error("O campo email é obrigatório.");
            throw new InvalidRequestException("O email é obrigatório.");
        }
        if (usuarioCreateDTO.getNome() == null || usuarioCreateDTO.getNome().isEmpty()) {
            logger.error("O campo nome é obrigatório.");
            throw new InvalidRequestException("O nome é obrigatório.");
        }
    }

    private void atualizarCampos(Usuario usuario, UsuarioCreateDTO usuarioCreateDTO) {
        if (usuarioCreateDTO.getNome() != null) {
            logger.info("Atualizando nome do usuário com ID: {}", usuario.getUsuarioId());
            usuario.setNome(usuarioCreateDTO.getNome());
        }
        if (usuarioCreateDTO.getEmail() != null && !usuarioCreateDTO.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmailAndUsuarioIdNot(usuarioCreateDTO.getEmail(), usuario.getUsuarioId())) {
                logger.warn("O email já está em uso: {}", usuarioCreateDTO.getEmail());
                throw new InvalidRequestException("O email já está em uso.");
            }
            usuario.setEmail(usuarioCreateDTO.getEmail());
        }
        if (usuarioCreateDTO.getSenha() != null) {
            logger.info("Atualizando senha do usuário com ID: {}", usuario.getUsuarioId());
            usuario.setSenha(usuarioCreateDTO.getSenha());
        }
        if (usuarioCreateDTO.getTelefone() != null) {
            logger.info("Atualizando telefone do usuário com ID: {}", usuario.getUsuarioId());
            usuario.setTelefone(usuarioCreateDTO.getTelefone());
        }
    }

    public void deletarUsuario(Long id) {
        logger.info("Deletando usuário com ID: {}", id);

        if (!usuarioRepository.existsById(id)) {
            logger.error("Usuário não encontrado com ID: {}", id);
            throw new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO + id);
        }

        usuarioRepository.deleteById(id);
        logger.info("Usuário com ID: {} deletado com sucesso.", id);
    }
}
