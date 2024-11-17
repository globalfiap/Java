package controller;

import dto.Veiculo.VeiculoCreateDTO;
import dto.Veiculo.VeiculoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;
import service.VeiculoService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    @Autowired
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    public CollectionModel<EntityModel<VeiculoDTO>> listarTodos() {
        List<EntityModel<VeiculoDTO>> veiculos = veiculoService.listarTodos().stream()
                .map(veiculoDTO -> EntityModel.of(veiculoDTO,
                        linkTo(methodOn(VeiculoController.class).obterVeiculo(veiculoDTO.getVeiculoId())).withSelfRel(),
                        linkTo(methodOn(VeiculoController.class).listarTodos()).withRel("veiculos")))
                .collect(Collectors.toList());

        return CollectionModel.of(veiculos, linkTo(methodOn(VeiculoController.class).listarTodos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<VeiculoDTO> obterVeiculo(@PathVariable Long id) {
        VeiculoDTO veiculoDTO = veiculoService.obterPorId(id);

        return EntityModel.of(veiculoDTO,
                linkTo(methodOn(VeiculoController.class).obterVeiculo(id)).withSelfRel(),
                linkTo(methodOn(VeiculoController.class).listarTodos()).withRel("veiculos"),
                linkTo(methodOn(VeiculoController.class).deletarVeiculo(id)).withRel("delete"),
                linkTo(methodOn(VeiculoController.class).atualizarVeiculo(id, null)).withRel("update"));
    }

    @PostMapping
    public EntityModel<VeiculoDTO> criarVeiculo(@Valid @RequestBody VeiculoCreateDTO veiculoCreateDTO) {
        VeiculoDTO veiculoDTO = veiculoService.criarVeiculo(veiculoCreateDTO);

        return EntityModel.of(veiculoDTO,
                linkTo(methodOn(VeiculoController.class).obterVeiculo(veiculoDTO.getVeiculoId())).withSelfRel(),
                linkTo(methodOn(VeiculoController.class).listarTodos()).withRel("veiculos"));
    }

    @PutMapping("/{id}")
    public EntityModel<VeiculoDTO> atualizarVeiculo(@PathVariable Long id, @Valid @RequestBody VeiculoCreateDTO veiculoCreateDTO) {
        VeiculoDTO veiculoDTO = veiculoService.atualizarVeiculo(id, veiculoCreateDTO);

        return EntityModel.of(veiculoDTO,
                linkTo(methodOn(VeiculoController.class).obterVeiculo(id)).withSelfRel(),
                linkTo(methodOn(VeiculoController.class).listarTodos()).withRel("veiculos"));
    }

    @DeleteMapping("/{id}")
    public EntityModel<Void> deletarVeiculo(@PathVariable Long id) {
        veiculoService.deletarVeiculo(id);

        return EntityModel.of(null,
                linkTo(methodOn(VeiculoController.class).listarTodos()).withRel("veiculos"));
    }
}
