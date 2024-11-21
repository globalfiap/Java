package com.java.EcoDrive.controller;

import com.java.EcoDrive.dto.Bairro.BairroCreateDTO;
import com.java.EcoDrive.dto.Bairro.BairroDTO;
import com.java.EcoDrive.service.BairroService;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/bairros")
@Tag(name = "Bairros", description = "Controle de Bairros")
@Validated
public class BairroController {

    private static final String LISTAR_TODOS = "listarTodos";

    private final BairroService bairroService;

    @Autowired
    public BairroController(BairroService bairroService) {
        this.bairroService = bairroService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os bairros", description = "Retorna uma lista paginada de todos os bairros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bairros retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public CollectionModel<EntityModel<BairroDTO>> listarTodos(
            @Parameter(description = "Página a ser exibida") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de elementos por página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BairroDTO> bairrosPaginados = bairroService.listarTodosPaginado(pageable);

        List<EntityModel<BairroDTO>> bairros = bairrosPaginados.getContent().stream()
                .map(bairro -> EntityModel.of(bairro,
                        linkTo(methodOn(BairroController.class).obterBairro(bairro.getBairroId())).withSelfRel()))
                .toList(); // Substituído por Stream.toList()

        return CollectionModel.of(bairros,
                linkTo(methodOn(BairroController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter um bairro específico", description = "Retorna os detalhes do bairro fornecendo o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bairro encontrado"),
            @ApiResponse(responseCode = "404", description = "Bairro não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<BairroDTO> obterBairro(
            @Parameter(description = "ID do bairro a ser obtido") @PathVariable Long id) {
        BairroDTO bairroDTO = bairroService.obterPorId(id);
        return EntityModel.of(bairroDTO,
                linkTo(methodOn(BairroController.class).obterBairro(id)).withSelfRel(),
                linkTo(methodOn(BairroController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar um novo bairro", description = "Cria um novo bairro com as informações fornecidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bairro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<BairroDTO> criarBairro(
            @Parameter(description = "Dados do bairro a ser criado") @Valid @RequestBody BairroCreateDTO bairroCreateDTO) {
        BairroDTO bairroDTO = bairroService.criarBairro(bairroCreateDTO);
        return EntityModel.of(bairroDTO,
                linkTo(methodOn(BairroController.class).obterBairro(bairroDTO.getBairroId())).withSelfRel(),
                linkTo(methodOn(BairroController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um bairro", description = "Atualiza as informações de um bairro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bairro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Bairro não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<BairroDTO> atualizarBairro(
            @Parameter(description = "ID do bairro a ser atualizado") @PathVariable Long id,
            @Parameter(description = "Dados atualizados do bairro") @Valid @RequestBody BairroCreateDTO bairroCreateDTO) {
        BairroDTO bairroDTO = bairroService.atualizarBairro(id, bairroCreateDTO);
        return EntityModel.of(bairroDTO,
                linkTo(methodOn(BairroController.class).obterBairro(id)).withSelfRel(),
                linkTo(methodOn(BairroController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar um bairro", description = "Remove um bairro com o ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bairro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Bairro não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public void deletarBairro(
            @Parameter(description = "ID do bairro a ser deletado") @PathVariable Long id) {
        bairroService.deletarBairro(id);
    }
}