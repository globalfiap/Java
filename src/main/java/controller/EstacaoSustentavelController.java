package controller;

import dto.EstacaoSustentavel.EstacaoSustentavelCreateDTO;
import dto.EstacaoSustentavel.EstacaoSustentavelDTO;
import service.EstacaoSustentavelService;
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
@RequestMapping("/estacoes-sustentaveis")
public class EstacaoSustentavelController {

    private final EstacaoSustentavelService estacaoSustentavelService;

    @Autowired
    public EstacaoSustentavelController(EstacaoSustentavelService estacaoSustentavelService) {
        this.estacaoSustentavelService = estacaoSustentavelService;
    }

    @GetMapping
    public CollectionModel<EntityModel<EstacaoSustentavelDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EstacaoSustentavelDTO> estacoesPaginadas = estacaoSustentavelService.listarTodosPaginado(pageable);

        List<EntityModel<EstacaoSustentavelDTO>> estacoes = estacoesPaginadas.getContent().stream()
                .map(estacao -> EntityModel.of(estacao,
                        linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(estacao.getEstacaoId())).withSelfRel(),
                        linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(page, size)).withRel("estacoes-sustentaveis")))
                .collect(Collectors.toList());

        return CollectionModel.of(estacoes, linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<EstacaoSustentavelDTO> obterPorId(@PathVariable Long id) {
        EstacaoSustentavelDTO estacaoDTO = estacaoSustentavelService.obterPorId(id);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(id)).withSelfRel(),
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(0, 10)).withRel("estacoes-sustentaveis"));
    }

    @PostMapping
    public EntityModel<EstacaoSustentavelDTO> criarEstacaoSustentavel(@RequestBody EstacaoSustentavelCreateDTO estacaoCreateDTO) {
        EstacaoSustentavelDTO estacaoDTO = estacaoSustentavelService.criarEstacaoSustentavel(estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(estacaoDTO.getEstacaoId())).withSelfRel(),
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(0, 10)).withRel("estacoes-sustentaveis"));
    }

    @PutMapping("/{id}")
    public EntityModel<EstacaoSustentavelDTO> atualizarEstacaoSustentavel(@PathVariable Long id, @RequestBody EstacaoSustentavelCreateDTO estacaoCreateDTO) {
        EstacaoSustentavelDTO estacaoDTO = estacaoSustentavelService.atualizarEstacaoSustentavel(id, estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(id)).withSelfRel(),
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(0, 10)).withRel("estacoes-sustentaveis"));
    }

    @DeleteMapping("/{id}")
    public EntityModel<Void> deletarEstacaoSustentavel(@PathVariable Long id) {
        estacaoSustentavelService.deletarEstacaoSustentavel(id);
        return EntityModel.of(null,
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(0, 10)).withRel("estacoes-sustentaveis"));
    }
}
