package com.java.EcoDrive.controller;

import com.java.EcoDrive.dto.EstacaoSustentavel.EstacaoSustentavelCreateDTO;
import com.java.EcoDrive.dto.EstacaoSustentavel.EstacaoSustentavelDTO;
import org.springframework.http.ResponseEntity;
import com.java.EcoDrive.service.EstacaoSustentavelService;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping(value ="/estacoes-sustentaveis", produces = "application/json")
@Tag(name = "Estações Sustentáveis", description = "Controle das Estações Sustentáveis")
public class EstacaoSustentavelController {

    private static final String LISTAR_TODOS = "listarTodos";

    private final EstacaoSustentavelService estacaoSustentavelService;

    @Autowired
    public EstacaoSustentavelController(EstacaoSustentavelService estacaoSustentavelService) {
        this.estacaoSustentavelService = estacaoSustentavelService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as estações sustentáveis", description = "Retorna uma lista paginada de todas as estações sustentáveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estações sustentáveis retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public CollectionModel<EntityModel<EstacaoSustentavelDTO>> listarTodos(
            @Parameter(description = "Página a ser exibida") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de elementos por página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EstacaoSustentavelDTO> estacoesPaginadas = estacaoSustentavelService.listarTodosPaginado(pageable);

        List<EntityModel<EstacaoSustentavelDTO>> estacoes = estacoesPaginadas.getContent().stream()
                .map(estacao -> EntityModel.of(estacao,
                        linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(estacao.getEstacaoId())).withSelfRel()))
                .toList(); // Substituído por Stream.toList()

        return CollectionModel.of(estacoes,
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma estação sustentável específica", description = "Retorna os detalhes de uma estação sustentável fornecendo o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estação sustentável encontrada"),
            @ApiResponse(responseCode = "404", description = "Estação sustentável não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<EstacaoSustentavelDTO> obterPorId(
            @Parameter(description = "ID da estação sustentável a ser obtida") @PathVariable Long id) {
        EstacaoSustentavelDTO estacaoDTO = estacaoSustentavelService.obterPorId(id);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(id)).withSelfRel(),
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @PostMapping
    @Operation(summary = "Criar uma nova estação sustentável", description = "Cria uma nova estação sustentável com as informações fornecidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estação sustentável criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<EstacaoSustentavelDTO> criarEstacaoSustentavel(
            @Parameter(description = "Dados da estação sustentável a ser criada") @Valid @RequestBody EstacaoSustentavelCreateDTO estacaoCreateDTO) {
        EstacaoSustentavelDTO estacaoDTO = estacaoSustentavelService.criarEstacaoSustentavel(estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(estacaoDTO.getEstacaoId())).withSelfRel(),
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma estação sustentável", description = "Atualiza as informações de uma estação sustentável existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estação sustentável atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Estação sustentável não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<EstacaoSustentavelDTO> atualizarEstacaoSustentavel(
            @Parameter(description = "ID da estação sustentável a ser atualizada") @PathVariable Long id,
            @Parameter(description = "Dados atualizados da estação sustentável") @Valid @RequestBody EstacaoSustentavelCreateDTO estacaoCreateDTO) {
        EstacaoSustentavelDTO estacaoDTO = estacaoSustentavelService.atualizarEstacaoSustentavel(id, estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(id)).withSelfRel(),
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma estação sustentável", description = "Remove uma estação sustentável fornecendo o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estação sustentável deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estação sustentável não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletarEstacaoSustentavel(
            @Parameter(description = "ID da estação sustentável a ser deletada") @PathVariable Long id) {
        estacaoSustentavelService.deletarEstacaoSustentavel(id);
        return ResponseEntity.noContent().build();
    }
}