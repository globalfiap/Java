package com.java.EcoDrive.controller;

import com.java.EcoDrive.dto.Veiculo.VeiculoCreateDTO;
import com.java.EcoDrive.dto.Veiculo.VeiculoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.java.EcoDrive.service.VeiculoService;

import jakarta.validation.Valid;
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
@RequestMapping(value = "/veiculos", produces = "application/json", consumes = "application/json")
@Tag(name = "Veículos", description = "Controle dos Veículos")
public class VeiculoController {

    private static final String VEICULOS_REL = "veiculos"; // Constante para evitar duplicação

    private final VeiculoService veiculoService;

    @Autowired
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os veículos", description = "Retorna uma lista paginada de todos os veículos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de veículos retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public CollectionModel<EntityModel<VeiculoDTO>> listarTodos(
            @Parameter(description = "Página a ser exibida") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de elementos por página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VeiculoDTO> veiculosPaginados = veiculoService.listarTodosPaginado(pageable);

        List<EntityModel<VeiculoDTO>> veiculos = veiculosPaginados.getContent().stream()
                .map(veiculoDTO -> EntityModel.of(veiculoDTO,
                        linkTo(methodOn(VeiculoController.class).obterVeiculo(veiculoDTO.getVeiculoId())).withSelfRel()))
                .toList(); // Substituição de collect(Collectors.toList()) por toList()

        return CollectionModel.of(veiculos, linkTo(methodOn(VeiculoController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Obter um veículo específico", description = "Retorna os detalhes de um veículo pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<VeiculoDTO> obterVeiculo(
            @Parameter(description = "ID do veículo a ser obtido") @PathVariable Long id) {
        VeiculoDTO veiculoDTO = veiculoService.obterPorId(id);

        return EntityModel.of(veiculoDTO,
                linkTo(methodOn(VeiculoController.class).obterVeiculo(id)).withSelfRel(),
                linkTo(methodOn(VeiculoController.class).listarTodos(0, 10)).withRel(VEICULOS_REL));
    }

    @PostMapping
    @Operation(summary = "Criar um novo veículo", description = "Cria um novo veículo com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<VeiculoDTO> criarVeiculo(
            @Parameter(description = "Dados do veículo a ser criado") @Valid @RequestBody VeiculoCreateDTO veiculoCreateDTO) {
        VeiculoDTO veiculoDTO = veiculoService.criarVeiculo(veiculoCreateDTO);

        return EntityModel.of(veiculoDTO,
                linkTo(methodOn(VeiculoController.class).obterVeiculo(veiculoDTO.getVeiculoId())).withSelfRel(),
                linkTo(methodOn(VeiculoController.class).listarTodos(0, 10)).withRel(VEICULOS_REL));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualizar um veículo", description = "Atualiza as informações de um veículo existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<VeiculoDTO> atualizarVeiculo(
            @Parameter(description = "ID do veículo a ser atualizado") @PathVariable Long id,
            @Parameter(description = "Dados atualizados do veículo") @Valid @RequestBody VeiculoCreateDTO veiculoCreateDTO) {
        VeiculoDTO veiculoDTO = veiculoService.atualizarVeiculo(id, veiculoCreateDTO);

        return EntityModel.of(veiculoDTO,
                linkTo(methodOn(VeiculoController.class).obterVeiculo(id)).withSelfRel(),
                linkTo(methodOn(VeiculoController.class).listarTodos(0, 10)).withRel(VEICULOS_REL));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletar um veículo", description = "Remove um veículo pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Veículo deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletarVeiculo(
            @Parameter(description = "ID do veículo a ser deletado") @PathVariable Long id) {
        veiculoService.deletarVeiculo(id);
        return ResponseEntity.noContent().build();
    }
}