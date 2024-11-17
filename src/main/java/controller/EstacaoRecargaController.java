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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/estacoes-recarga")
@Api(value = "Estação de Recarga Controller", tags = {"Estações de Recarga"})
public class EstacaoRecargaController {

    private final EstacaoRecargaService estacaoRecargaService;

    @Autowired
    public EstacaoRecargaController(EstacaoRecargaService estacaoRecargaService) {
        this.estacaoRecargaService = estacaoRecargaService;
    }

    @GetMapping
    @ApiOperation(value = "Listar todas as estações de recarga", notes = "Retorna uma lista paginada de todas as estações de recarga")
    public CollectionModel<EntityModel<EstacaoRecargaDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EstacaoRecargaDTO> estacoesPaginadas = estacaoRecargaService.listarTodosPaginado(pageable);

        List<EntityModel<EstacaoRecargaDTO>> estacoes = estacoesPaginadas.getContent().stream()
                .map(estacao -> EntityModel.of(estacao,
                        linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(estacao.getEstacaoId())).withSelfRel(),
                        linkTo(methodOn(EstacaoRecargaController.class).listarTodos(page, size)).withRel("estacoes-recarga")))
                .collect(Collectors.toList());

        return CollectionModel.of(estacoes, linkTo(methodOn(EstacaoRecargaController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obter uma estação de recarga específica", notes = "Retorna os detalhes de uma estação de recarga fornecendo o ID")
    public EntityModel<EstacaoRecargaDTO> obterEstacaoRecarga(@PathVariable Long id) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.obterPorId(id);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(id)).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(0, 10)).withRel("estacoes-recarga"));
    }

    @PostMapping
    @ApiOperation(value = "Criar uma nova estação de recarga", notes = "Cria uma nova estação de recarga com as informações fornecidas")
    public EntityModel<EstacaoRecargaDTO> criarEstacaoRecarga(@RequestBody EstacaoRecargaCreateDTO estacaoCreateDTO) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.criarEstacaoRecarga(estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(estacaoDTO.getEstacaoId())).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(0, 10)).withRel("estacoes-recarga"));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualizar uma estação de recarga", notes = "Atualiza as informações de uma estação de recarga existente")
    public EntityModel<EstacaoRecargaDTO> atualizarEstacaoRecarga(@PathVariable Long id, @RequestBody EstacaoRecargaCreateDTO estacaoCreateDTO) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.atualizarEstacaoRecarga(id, estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(id)).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(0, 10)).withRel("estacoes-recarga"));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletar uma estação de recarga", notes = "Remove uma estação de recarga fornecendo o ID")
    public EntityModel<Void> deletarEstacaoRecarga(@PathVariable Long id) {
        estacaoRecargaService.deletarEstacaoRecarga(id);
        return EntityModel.of(null,
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(0, 10)).withRel("estacoes-recarga"));
    }
}

