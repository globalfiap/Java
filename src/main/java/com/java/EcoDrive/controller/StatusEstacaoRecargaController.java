package com.java.EcoDrive.controller;

import com.java.EcoDrive.model.StatusEstacaoRecarga;
import com.java.EcoDrive.service.StatusEstacaoRecargaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping(value = "/status-estacoes", produces = "application/json", consumes = "application/json")
@Tag(name = "Status Estação Recarga", description = "Controlador para Status das Estações de Recarga")
public class StatusEstacaoRecargaController {

    private final StatusEstacaoRecargaService statusEstacaoRecargaService;

    @Autowired
    public StatusEstacaoRecargaController(StatusEstacaoRecargaService statusEstacaoRecargaService) {
        this.statusEstacaoRecargaService = statusEstacaoRecargaService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os status das estações", description = "Retorna uma lista paginada de todos os status das estações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de status retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public CollectionModel<EntityModel<StatusEstacaoRecarga>> listarTodos(
            @Parameter(description = "Página a ser exibida") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de elementos por página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StatusEstacaoRecarga> statusPaginados = statusEstacaoRecargaService.listarTodosPaginado(pageable);

        List<EntityModel<StatusEstacaoRecarga>> statusList = statusPaginados.getContent().stream()
                .map(status -> EntityModel.of(status,
                        linkTo(methodOn(StatusEstacaoRecargaController.class).obterStatus(status.getStatusId())).withSelfRel()))
                .toList();

        return CollectionModel.of(statusList,
                linkTo(methodOn(StatusEstacaoRecargaController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Obter um status específico", description = "Retorna os detalhes de um status pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status encontrado"),
            @ApiResponse(responseCode = "404", description = "Status não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<StatusEstacaoRecarga> obterStatus(
            @Parameter(description = "ID do status a ser obtido") @PathVariable Long id) {
        StatusEstacaoRecarga status = statusEstacaoRecargaService.obterPorId(id);

        return EntityModel.of(status,
                linkTo(methodOn(StatusEstacaoRecargaController.class).obterStatus(id)).withSelfRel(),
                linkTo(methodOn(StatusEstacaoRecargaController.class).listarTodos(0, 10)).withRel("status-estacoes"));
    }

    @PostMapping
    @Operation(summary = "Criar um novo status", description = "Cria um novo status para uma estação de recarga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Status criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<StatusEstacaoRecarga> criarStatus(
            @Parameter(description = "Dados do status a ser criado") @Valid @RequestBody StatusEstacaoRecarga statusEstacaoRecarga) {
        StatusEstacaoRecarga statusCriado = statusEstacaoRecargaService.criarStatus(statusEstacaoRecarga);

        return EntityModel.of(statusCriado,
                linkTo(methodOn(StatusEstacaoRecargaController.class).obterStatus(statusCriado.getStatusId())).withSelfRel(),
                linkTo(methodOn(StatusEstacaoRecargaController.class).listarTodos(0, 10)).withRel("status-estacoes"));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualizar um status", description = "Atualiza as informações de um status existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Status não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<StatusEstacaoRecarga> atualizarStatus(
            @Parameter(description = "ID do status a ser atualizado") @PathVariable Long id,
            @Parameter(description = "Dados atualizados do status") @Valid @RequestBody StatusEstacaoRecarga statusAtualizado) {
        StatusEstacaoRecarga status = statusEstacaoRecargaService.atualizarStatus(id, statusAtualizado);

        return EntityModel.of(status,
                linkTo(methodOn(StatusEstacaoRecargaController.class).obterStatus(id)).withSelfRel(),
                linkTo(methodOn(StatusEstacaoRecargaController.class).listarTodos(0, 10)).withRel("status-estacoes"));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletar um status", description = "Remove um status pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Status deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Status não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletarStatus(
            @Parameter(description = "ID do status a ser deletado") @PathVariable Long id) {
        statusEstacaoRecargaService.deletarStatus(id);
        return ResponseEntity.noContent().build();
    }
}
