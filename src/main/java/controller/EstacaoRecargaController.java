package controller;

import dto.EstacaoRecarga.EstacaoRecargaCreateDTO;
import dto.EstacaoRecarga.EstacaoRecargaDTO;
import service.EstacaoRecargaService;
import exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/estacoes-recarga")
public class EstacaoRecargaController {

    private final EstacaoRecargaService estacaoRecargaService;

    @Autowired
    public EstacaoRecargaController(EstacaoRecargaService estacaoRecargaService) {
        this.estacaoRecargaService = estacaoRecargaService;
    }

    @GetMapping
    public CollectionModel<EntityModel<EstacaoRecargaDTO>> listarTodos() {
        List<EntityModel<EstacaoRecargaDTO>> estacoes = estacaoRecargaService.listarTodos().stream()
                .map(estacao -> EntityModel.of(estacao,
                        linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(estacao.getEstacaoId())).withSelfRel(),
                        linkTo(methodOn(EstacaoRecargaController.class).listarTodos()).withRel("estacoes-recarga")))
                .collect(Collectors.toList());

        return CollectionModel.of(estacoes, linkTo(methodOn(EstacaoRecargaController.class).listarTodos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<EstacaoRecargaDTO> obterEstacaoRecarga(@PathVariable Long id) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.obterPorId(id);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(id)).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos()).withRel("estacoes-recarga"));
    }

    @PostMapping
    public EntityModel<EstacaoRecargaDTO> criarEstacaoRecarga(@RequestBody EstacaoRecargaCreateDTO estacaoCreateDTO) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.criarEstacaoRecarga(estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(estacaoDTO.getEstacaoId())).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos()).withRel("estacoes-recarga"));
    }

    @PutMapping("/{id}")
    public EntityModel<EstacaoRecargaDTO> atualizarEstacaoRecarga(@PathVariable Long id, @RequestBody EstacaoRecargaCreateDTO estacaoCreateDTO) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.atualizarEstacaoRecarga(id, estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(id)).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos()).withRel("estacoes-recarga"));
    }

    @DeleteMapping("/{id}")
    public EntityModel<Void> deletarEstacaoRecarga(@PathVariable Long id) {
        estacaoRecargaService.deletarEstacaoRecarga(id);
        return EntityModel.of(null,
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos()).withRel("estacoes-recarga"));
    }
}
