package com.java.EcoDrive.controller;

import com.java.EcoDrive.dto.Usuario.UsuarioCreateDTO;
import com.java.EcoDrive.dto.Usuario.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import com.java.EcoDrive.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/usuarios", produces = "application/json", consumes = "application/json")
@Tag(name = "Usuários", description = "Controle dos Usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Definindo constante para "usuarios"
    private static final String USUARIOS_REL = "usuarios";

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista paginada de todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public CollectionModel<EntityModel<UsuarioDTO>> listarTodos(
            @Parameter(description = "Página a ser exibida") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de elementos por página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioDTO> usuariosPaginados = usuarioService.listarTodosPaginado(pageable);

        List<EntityModel<UsuarioDTO>> usuarios = usuariosPaginados.stream()
                .map(usuarioDTO -> EntityModel.of(usuarioDTO,
                        linkTo(methodOn(UsuarioController.class).obterUsuario(usuarioDTO.getUsuarioId())).withSelfRel()))
                .toList(); // Substituição de collect(Collectors.toList()) por toList()

        return CollectionModel.of(usuarios, linkTo(methodOn(UsuarioController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Obter um usuário específico", description = "Retorna os detalhes de um usuário pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<UsuarioDTO> obterUsuario(
            @Parameter(description = "ID do usuário a ser obtido") @PathVariable Long id) {
        UsuarioDTO usuarioDTO = usuarioService.obterPorId(id);

        return EntityModel.of(usuarioDTO,
                linkTo(methodOn(UsuarioController.class).obterUsuario(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos(0, 10)).withRel(USUARIOS_REL)); // Usando constante
    }

    @PostMapping
    @Operation(summary = "Criar um novo usuário", description = "Cria um novo usuário com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<UsuarioDTO> criarUsuario(
            @Parameter(description = "Dados do usuário a ser criado") @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioDTO usuarioDTO = usuarioService.criarUsuario(usuarioCreateDTO);

        return EntityModel.of(usuarioDTO,
                linkTo(methodOn(UsuarioController.class).obterUsuario(usuarioDTO.getUsuarioId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos(0, 10)).withRel(USUARIOS_REL)); // Usando constante
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualizar um usuário", description = "Atualiza as informações de um usuário existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<UsuarioDTO> atualizarUsuario(
            @Parameter(description = "ID do usuário a ser atualizado") @PathVariable Long id,
            @Parameter(description = "Dados atualizados do usuário") @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioDTO usuarioDTO = usuarioService.atualizarUsuario(id, usuarioCreateDTO);

        return EntityModel.of(usuarioDTO,
                linkTo(methodOn(UsuarioController.class).obterUsuario(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos(0, 10)).withRel(USUARIOS_REL)); // Usando constante
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletar um usuário", description = "Remove um usuário pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletarUsuario(
            @Parameter(description = "ID do usuário a ser deletado") @PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}