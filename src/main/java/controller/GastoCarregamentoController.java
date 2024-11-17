package controller;

import dto.GastoCarregamento.GastoCarregamentoCreateDTO;
import dto.GastoCarregamento.GastoCarregamentoDTO;
import service.GastoCarregamentoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/gastos-carregamento")
public class GastoCarregamentoController {

    private final GastoCarregamentoService gastoCarregamentoService;

    @Autowired
    public GastoCarregamentoController(GastoCarregamentoService gastoCarregamentoService) {
        this.gastoCarregamentoService = gastoCarregamentoService;
    }

    @GetMapping
    public CollectionModel<EntityModel<GastoCarregamentoDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GastoCarregamentoDTO> gastosPaginados = gastoCarregamentoService.listarTodosPaginado(pageable);

        List<EntityModel<GastoCarregamentoDTO>> gastos = gastosPaginados.getContent().stream()
                .map(gasto -> EntityModel.of(gasto,
                        linkTo(methodOn(GastoCarregamentoController.class).obterGastoCarregamento(gasto.getGastoId())).withSelfRel(),
                        linkTo(methodOn(GastoCarregamentoController.class).listarTodos(page, size)).withRel("gastos-carregamento")))
                .collect(Collectors.toList());

        return CollectionModel.of(gastos, linkTo(methodOn(GastoCarregamentoController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<GastoCarregamentoDTO> obterGastoCarregamento(@PathVariable Long id) {
        GastoCarregamentoDTO gastoDTO = gastoCarregamentoService.obterPorId(id);
        return EntityModel.of(gastoDTO,
                linkTo(methodOn(GastoCarregamentoController.class).obterGastoCarregamento(id)).withSelfRel(),
                linkTo(methodOn(GastoCarregamentoController.class).listarTodos(0, 10)).withRel("gastos-carregamento"));
    }

    @PostMapping
    public EntityModel<GastoCarregamentoDTO> criarGastoCarregamento(@RequestBody GastoCarregamentoCreateDTO gastoCreateDTO) {
        GastoCarregamentoDTO gastoDTO = gastoCarregamentoService.criarGastoCarregamento(gastoCreateDTO);
        return EntityModel.of(gastoDTO,
                linkTo(methodOn(GastoCarregamentoController.class).obterGastoCarregamento(gastoDTO.getGastoId())).withSelfRel(),
                linkTo(methodOn(GastoCarregamentoController.class).listarTodos(0, 10)).withRel("gastos-carregamento"));
    }

    @PutMapping("/{id}")
    public EntityModel<GastoCarregamentoDTO> atualizarGastoCarregamento(@PathVariable Long id, @RequestBody GastoCarregamentoCreateDTO gastoCreateDTO) {
        GastoCarregamentoDTO gastoDTO = gastoCarregamentoService.atualizarGastoCarregamento(id, gastoCreateDTO);
        return EntityModel.of(gastoDTO,
                linkTo(methodOn(GastoCarregamentoController.class).obterGastoCarregamento(id)).withSelfRel(),
                linkTo(methodOn(GastoCarregamentoController.class).listarTodos(0, 10)).withRel("gastos-carregamento"));
    }

    @DeleteMapping("/{id}")
    public EntityModel<Void> deletarGastoCarregamento(@PathVariable Long id) {
        gastoCarregamentoService.deletarGastoCarregamento(id);
        return EntityModel.of(null,
                linkTo(methodOn(GastoCarregamentoController.class).listarTodos(0, 10)).withRel("gastos-carregamento"));
    }
}
