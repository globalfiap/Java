package com.java.EcoDrive.controller;

import com.java.EcoDrive.dto.HistoricoCarregamento.HistoricoCarregamentoCreateDTO;
import com.java.EcoDrive.dto.HistoricoCarregamento.HistoricoCarregamentoDTO;
import org.springframework.http.ResponseEntity;
import com.java.EcoDrive.service.HistoricoCarregamentoService;
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

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/historico-carregamento")
@Tag(name = "Histórico de Carregamento", description = "Histórico de Carregamento Controller")
public class HistoricoCarregamentoController {

    private static final String LISTAR_TODOS = "listarTodos";

    private final HistoricoCarregamentoService historicoCarregamentoService;

    @Autowired
    public HistoricoCarregamentoController(HistoricoCarregamentoService historicoCarregamentoService) {
        this.historicoCarregamentoService = historicoCarregamentoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os históricos de carregamento", description = "Retorna uma lista paginada de todos os históricos de carregamento disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de históricos de carregamento retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public CollectionModel<EntityModel<HistoricoCarregamentoDTO>> listarTodos(
            @Parameter(description = "Página a ser exibida") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de elementos por página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoricoCarregamentoDTO> historicosPaginados = historicoCarregamentoService.listarTodosPaginado(pageable);

        List<EntityModel<HistoricoCarregamentoDTO>> historicos = historicosPaginados.getContent().stream()
                .map(historico -> EntityModel.of(historico,
                        linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(historico.getHistoricoId())).withSelfRel()))
                .toList(); // Substituído Stream.collect(Collectors.toList()) por Stream.toList()

        return CollectionModel.of(historicos,
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter um histórico de carregamento específico", description = "Retorna os detalhes de um histórico de carregamento pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de carregamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Histórico de carregamento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<HistoricoCarregamentoDTO> obterHistorico(
            @Parameter(description = "ID do histórico de carregamento a ser obtido") @PathVariable Long id) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.obterPorId(id);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(id)).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @PostMapping
    @Operation(summary = "Criar um novo histórico de carregamento", description = "Cria um novo histórico de carregamento com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Histórico de carregamento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<HistoricoCarregamentoDTO> criarHistoricoCarregamento(
            @Parameter(description = "Dados do histórico de carregamento a ser criado") @RequestBody HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.criarHistorico(historicoCreateDTO);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(historicoDTO.getHistoricoId())).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um histórico de carregamento", description = "Atualiza as informações de um histórico de carregamento existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de carregamento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Histórico de carregamento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<HistoricoCarregamentoDTO> atualizarHistorico(
            @Parameter(description = "ID do histórico de carregamento a ser atualizado") @PathVariable Long id,
            @Parameter(description = "Dados atualizados do histórico de carregamento") @RequestBody HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.atualizarHistorico(id, historicoCreateDTO);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(id)).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um histórico de carregamento", description = "Remove um histórico de carregamento pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Histórico de carregamento deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Histórico de carregamento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletarHistorico(
            @Parameter(description = "ID do histórico de carregamento a ser deletado") @PathVariable Long id) {
        historicoCarregamentoService.deletarHistorico(id);
        return ResponseEntity.noContent().build();
    }
}