package controller;


import dto.Usuario.UsuarioCreateDTO;
import dto.Usuario.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import service.UsuarioService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public CollectionModel<EntityModel<UsuarioDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioDTO> usuariosPaginados = usuarioService.listarTodosPaginado(pageable);

        List<EntityModel<UsuarioDTO>> usuarios = usuariosPaginados.stream()
                .map(usuarioDTO -> EntityModel.of(usuarioDTO,
                        linkTo(methodOn(UsuarioController.class).obterUsuario(usuarioDTO.getUsuarioId())).withSelfRel(),
                        linkTo(methodOn(UsuarioController.class).listarTodos(page, size)).withRel("usuarios")))
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios, linkTo(methodOn(UsuarioController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<UsuarioDTO> obterUsuario(@PathVariable Long id) {
        UsuarioDTO usuarioDTO = usuarioService.obterPorId(id);

        return EntityModel.of(usuarioDTO,
                linkTo(methodOn(UsuarioController.class).obterUsuario(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos(0, 10)).withRel("usuarios"),
                linkTo(methodOn(UsuarioController.class).deletarUsuario(id)).withRel("delete"),
                linkTo(methodOn(UsuarioController.class).atualizarUsuario(id, null)).withRel("update"));
    }

    @PostMapping
    public EntityModel<UsuarioDTO> criarUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioDTO usuarioDTO = usuarioService.criarUsuario(usuarioCreateDTO);

        return EntityModel.of(usuarioDTO,
                linkTo(methodOn(UsuarioController.class).obterUsuario(usuarioDTO.getUsuarioId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos(0, 10)).withRel("usuarios"));
    }

    @PutMapping("/{id}")
    public EntityModel<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioDTO usuarioDTO = usuarioService.atualizarUsuario(id, usuarioCreateDTO);

        return EntityModel.of(usuarioDTO,
                linkTo(methodOn(UsuarioController.class).obterUsuario(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos(0, 10)).withRel("usuarios"));
    }

    @DeleteMapping("/{id}")
    public EntityModel<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);

        return EntityModel.of(null,
                linkTo(methodOn(UsuarioController.class).listarTodos(0, 10)).withRel("usuarios"));
    }
}

