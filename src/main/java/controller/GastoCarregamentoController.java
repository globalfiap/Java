package controller;

import dto.GastoCarregamento.GastoCarregamentoCreateDTO;
import dto.GastoCarregamento.GastoCarregamentoDTO;
import org.springframework.http.ResponseEntity;
import service.GastoCarregamentoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/gastos-carregamento")
@Tag(name = "Gastos de Carregamento", description = "Gasto de Carregamento Controller")
public class GastoCarregamentoController {

    private final GastoCarregamentoService gastoCarregamentoService;

    @Autowired
    public GastoCarregamentoController(GastoCarregamentoService gastoCarregamentoService) {
        this.gastoCarregamentoService = gastoCarregamentoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os gastos de carregamento", description = "Retorna uma lista paginada de todos os gastos de carregamento disponíveis")
    public CollectionModel<EntityModel<GastoCarregamentoDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GastoCarregamentoDTO> gastosPaginados = gastoCarregamentoService.listarTodosPaginado(pageable);

        List<EntityModel<GastoCarregamentoDTO>> gastos = gastosPaginados.getContent().stream()
                .map(gasto -> EntityModel.of(gasto,
                        linkTo(methodOn(GastoCarregamentoController.class).obterGastoCarregamento(gasto.getGastoId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(gastos, linkTo(methodOn(GastoCarregamentoController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter um gasto de carregamento específico", description = "Retorna os detalhes de um gasto de carregamento pelo seu ID")
    public EntityModel<GastoCarregamentoDTO> obterGastoCarregamento(@PathVariable Long id) {
        GastoCarregamentoDTO gastoDTO = gastoCarregamentoService.obterPorId(id);
        return EntityModel.of(gastoDTO,
                linkTo(methodOn(GastoCarregamentoController.class).obterGastoCarregamento(id)).withSelfRel(),
                linkTo(methodOn(GastoCarregamentoController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PostMapping
    @Operation(summary = "Criar um novo gasto de carregamento", description = "Cria um novo gasto de carregamento com os dados fornecidos")
    public EntityModel<GastoCarregamentoDTO> criarGastoCarregamento(@Valid @RequestBody GastoCarregamentoCreateDTO gastoCreateDTO) {
        GastoCarregamentoDTO gastoDTO = gastoCarregamentoService.criarGastoCarregamento(gastoCreateDTO);
        return EntityModel.of(gastoDTO,
                linkTo(methodOn(GastoCarregamentoController.class).obterGastoCarregamento(gastoDTO.getGastoId())).withSelfRel(),
                linkTo(methodOn(GastoCarregamentoController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um gasto de carregamento", description = "Atualiza as informações de um gasto de carregamento existente pelo seu ID")
    public EntityModel<GastoCarregamentoDTO> atualizarGastoCarregamento(@PathVariable Long id, @Valid @RequestBody GastoCarregamentoCreateDTO gastoCreateDTO) {
        GastoCarregamentoDTO gastoDTO = gastoCarregamentoService.atualizarGastoCarregamento(id, gastoCreateDTO);
        return EntityModel.of(gastoDTO,
                linkTo(methodOn(GastoCarregamentoController.class).obterGastoCarregamento(id)).withSelfRel(),
                linkTo(methodOn(GastoCarregamentoController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um gasto de carregamento", description = "Remove um gasto de carregamento pelo seu ID")
    public ResponseEntity<Void> deletarGastoCarregamento(@PathVariable Long id) {
        gastoCarregamentoService.deletarGastoCarregamento(id);
        return ResponseEntity.noContent().build();
    }
}
