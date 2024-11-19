package controller;

import dto.Veiculo.VeiculoCreateDTO;
import dto.Veiculo.VeiculoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.VeiculoService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/veiculos")
@Tag(name = "Veículos", description = "Controle dos Veículos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    @Autowired
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os veículos", description = "Retorna uma lista paginada de todos os veículos")
    public CollectionModel<EntityModel<VeiculoDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VeiculoDTO> veiculosPaginados = veiculoService.listarTodosPaginado(pageable);

        List<EntityModel<VeiculoDTO>> veiculos = veiculosPaginados.getContent().stream()
                .map(veiculoDTO -> EntityModel.of(veiculoDTO,
                        linkTo(methodOn(VeiculoController.class).obterVeiculo(veiculoDTO.getVeiculoId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(veiculos, linkTo(methodOn(VeiculoController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter um veículo específico", description = "Retorna os detalhes de um veículo pelo seu ID")
    public EntityModel<VeiculoDTO> obterVeiculo(@PathVariable Long id) {
        VeiculoDTO veiculoDTO = veiculoService.obterPorId(id);

        return EntityModel.of(veiculoDTO,
                linkTo(methodOn(VeiculoController.class).obterVeiculo(id)).withSelfRel(),
                linkTo(methodOn(VeiculoController.class).listarTodos(0, 10)).withRel("veiculos"));
    }

    @PostMapping
    @Operation(summary = "Criar um novo veículo", description = "Cria um novo veículo com os dados fornecidos")
    public EntityModel<VeiculoDTO> criarVeiculo(@Valid @RequestBody VeiculoCreateDTO veiculoCreateDTO) {
        VeiculoDTO veiculoDTO = veiculoService.criarVeiculo(veiculoCreateDTO);

        return EntityModel.of(veiculoDTO,
                linkTo(methodOn(VeiculoController.class).obterVeiculo(veiculoDTO.getVeiculoId())).withSelfRel(),
                linkTo(methodOn(VeiculoController.class).listarTodos(0, 10)).withRel("veiculos"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um veículo", description = "Atualiza as informações de um veículo existente pelo seu ID")
    public EntityModel<VeiculoDTO> atualizarVeiculo(@PathVariable Long id, @Valid @RequestBody VeiculoCreateDTO veiculoCreateDTO) {
        VeiculoDTO veiculoDTO = veiculoService.atualizarVeiculo(id, veiculoCreateDTO);

        return EntityModel.of(veiculoDTO,
                linkTo(methodOn(VeiculoController.class).obterVeiculo(id)).withSelfRel(),
                linkTo(methodOn(VeiculoController.class).listarTodos(0, 10)).withRel("veiculos"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um veículo", description = "Remove um veículo pelo seu ID")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        veiculoService.deletarVeiculo(id);
        return ResponseEntity.noContent().build();
    }

}
