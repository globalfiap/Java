package controller;

import dto.EstacaoSustentavel.EstacaoSustentavelCreateDTO;
import dto.EstacaoSustentavel.EstacaoSustentavelDTO;
import org.springframework.http.ResponseEntity;
import service.EstacaoSustentavelService;
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
@RequestMapping("/estacoes-sustentaveis")
@Tag(name = "Estações Sustentáveis", description = "Controle das Estações Sustentáveis")
public class EstacaoSustentavelController {

    private final EstacaoSustentavelService estacaoSustentavelService;

    @Autowired
    public EstacaoSustentavelController(EstacaoSustentavelService estacaoSustentavelService) {
        this.estacaoSustentavelService = estacaoSustentavelService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as estações sustentáveis", description = "Retorna uma lista paginada de todas as estações sustentáveis")
    public CollectionModel<EntityModel<EstacaoSustentavelDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EstacaoSustentavelDTO> estacoesPaginadas = estacaoSustentavelService.listarTodosPaginado(pageable);

        List<EntityModel<EstacaoSustentavelDTO>> estacoes = estacoesPaginadas.getContent().stream()
                .map(estacao -> EntityModel.of(estacao,
                        linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(estacao.getEstacaoId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(estacoes,
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma estação sustentável específica", description = "Retorna os detalhes de uma estação sustentável fornecendo o ID")
    public EntityModel<EstacaoSustentavelDTO> obterPorId(@PathVariable Long id) {
        EstacaoSustentavelDTO estacaoDTO = estacaoSustentavelService.obterPorId(id);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(id)).withSelfRel(),
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PostMapping
    @Operation(summary = "Criar uma nova estação sustentável", description = "Cria uma nova estação sustentável com as informações fornecidas")
    public EntityModel<EstacaoSustentavelDTO> criarEstacaoSustentavel(@Valid @RequestBody EstacaoSustentavelCreateDTO estacaoCreateDTO) {
        EstacaoSustentavelDTO estacaoDTO = estacaoSustentavelService.criarEstacaoSustentavel(estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(estacaoDTO.getEstacaoId())).withSelfRel(),
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma estação sustentável", description = "Atualiza as informações de uma estação sustentável existente")
    public EntityModel<EstacaoSustentavelDTO> atualizarEstacaoSustentavel(@PathVariable Long id, @Valid @RequestBody EstacaoSustentavelCreateDTO estacaoCreateDTO) {
        EstacaoSustentavelDTO estacaoDTO = estacaoSustentavelService.atualizarEstacaoSustentavel(id, estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoSustentavelController.class).obterPorId(id)).withSelfRel(),
                linkTo(methodOn(EstacaoSustentavelController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma estação sustentável", description = "Remove uma estação sustentável fornecendo o ID")
    public ResponseEntity<Void> deletarEstacaoSustentavel(@PathVariable Long id) {
        estacaoSustentavelService.deletarEstacaoSustentavel(id);
        return ResponseEntity.noContent().build();
    }

}
