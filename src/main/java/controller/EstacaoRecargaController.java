package controller;

import dto.EstacaoRecarga.EstacaoRecargaCreateDTO;
import dto.EstacaoRecarga.EstacaoRecargaDTO;
import service.EstacaoRecargaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/estacoes-recarga")
@Tag(name = "Estações de Recarga", description = "Controle das Estações de Recarga")
@Validated
public class EstacaoRecargaController {

    private final EstacaoRecargaService estacaoRecargaService;

    @Autowired
    public EstacaoRecargaController(EstacaoRecargaService estacaoRecargaService) {
        this.estacaoRecargaService = estacaoRecargaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as estações de recarga", description = "Retorna uma lista paginada de todas as estações de recarga")
    public CollectionModel<EntityModel<EstacaoRecargaDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EstacaoRecargaDTO> estacoesPaginadas = estacaoRecargaService.listarTodosPaginado(pageable);

        List<EntityModel<EstacaoRecargaDTO>> estacoes = estacoesPaginadas.getContent().stream()
                .map(estacao -> EntityModel.of(estacao,
                        linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(estacao.getEstacaoId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(estacoes,
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma estação de recarga específica", description = "Retorna os detalhes de uma estação de recarga fornecendo o ID")
    public EntityModel<EstacaoRecargaDTO> obterEstacaoRecarga(@PathVariable Long id) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.obterPorId(id);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(id)).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar uma nova estação de recarga", description = "Cria uma nova estação de recarga com as informações fornecidas")
    public EntityModel<EstacaoRecargaDTO> criarEstacaoRecarga(@Valid @RequestBody EstacaoRecargaCreateDTO estacaoCreateDTO) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.criarEstacaoRecarga(estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(estacaoDTO.getEstacaoId())).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma estação de recarga", description = "Atualiza as informações de uma estação de recarga existente")
    public EntityModel<EstacaoRecargaDTO> atualizarEstacaoRecarga(@PathVariable Long id, @Valid @RequestBody EstacaoRecargaCreateDTO estacaoCreateDTO) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.atualizarEstacaoRecarga(id, estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(id)).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar uma estação de recarga", description = "Remove uma estação de recarga fornecendo o ID")
    public void deletarEstacaoRecarga(@PathVariable Long id) {
        estacaoRecargaService.deletarEstacaoRecarga(id);
    }
}
