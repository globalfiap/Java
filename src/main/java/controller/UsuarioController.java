package controller;

import dto.Usuario.UsuarioCreateDTO;
import dto.Usuario.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Controle dos Usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista paginada de todos os usuários")
    public CollectionModel<EntityModel<UsuarioDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioDTO> usuariosPaginados = usuarioService.listarTodosPaginado(pageable);

        List<EntityModel<UsuarioDTO>> usuarios = usuariosPaginados.stream()
                .map(usuarioDTO -> EntityModel.of(usuarioDTO,
                        linkTo(methodOn(UsuarioController.class).obterUsuario(usuarioDTO.getUsuarioId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios, linkTo(methodOn(UsuarioController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter um usuário específico", description = "Retorna os detalhes de um usuário pelo seu ID")
    public EntityModel<UsuarioDTO> obterUsuario(@PathVariable Long id) {
        UsuarioDTO usuarioDTO = usuarioService.obterPorId(id);

        return EntityModel.of(usuarioDTO,
                linkTo(methodOn(UsuarioController.class).obterUsuario(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos(0, 10)).withRel("usuarios"));
    }

    @PostMapping
    @Operation(summary = "Criar um novo usuário", description = "Cria um novo usuário com os dados fornecidos")
    public EntityModel<UsuarioDTO> criarUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioDTO usuarioDTO = usuarioService.criarUsuario(usuarioCreateDTO);

        return EntityModel.of(usuarioDTO,
                linkTo(methodOn(UsuarioController.class).obterUsuario(usuarioDTO.getUsuarioId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos(0, 10)).withRel("usuarios"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um usuário", description = "Atualiza as informações de um usuário existente pelo seu ID")
    public EntityModel<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioDTO usuarioDTO = usuarioService.atualizarUsuario(id, usuarioCreateDTO);

        return EntityModel.of(usuarioDTO,
                linkTo(methodOn(UsuarioController.class).obterUsuario(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos(0, 10)).withRel("usuarios"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um usuário", description = "Remove um usuário pelo seu ID")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
