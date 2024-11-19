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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/estacoes-recarga")
@Tag(name = "Estações de Recarga", description = "Controle das Estações de Recarga")
@Validated
public class EstacaoRecargaController {

    private static final String LISTAR_TODOS = "listarTodos";

    private final EstacaoRecargaService estacaoRecargaService;

    @Autowired
    public EstacaoRecargaController(EstacaoRecargaService estacaoRecargaService) {
        this.estacaoRecargaService = estacaoRecargaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as estações de recarga", description = "Retorna uma lista paginada de todas as estações de recarga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estações de recarga retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public CollectionModel<EntityModel<EstacaoRecargaDTO>> listarTodos(
            @Parameter(description = "Página a ser exibida") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de elementos por página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EstacaoRecargaDTO> estacoesPaginadas = estacaoRecargaService.listarTodosPaginado(pageable);

        List<EntityModel<EstacaoRecargaDTO>> estacoes = estacoesPaginadas.getContent().stream()
                .map(estacao -> EntityModel.of(estacao,
                        linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(estacao.getEstacaoId())).withSelfRel()))
                .toList(); // Substituído Stream.collect(Collectors.toList()) por toList()

        return CollectionModel.of(estacoes,
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma estação de recarga específica", description = "Retorna os detalhes de uma estação de recarga fornecendo o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estação de recarga encontrada"),
            @ApiResponse(responseCode = "404", description = "Estação de recarga não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<EstacaoRecargaDTO> obterEstacaoRecarga(
            @Parameter(description = "ID da estação de recarga a ser obtida") @PathVariable Long id) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.obterPorId(id);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(id)).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar uma nova estação de recarga", description = "Cria uma nova estação de recarga com as informações fornecidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estação de recarga criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<EstacaoRecargaDTO> criarEstacaoRecarga(
            @Parameter(description = "Dados da estação de recarga a ser criada") @Valid @RequestBody EstacaoRecargaCreateDTO estacaoCreateDTO) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.criarEstacaoRecarga(estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(estacaoDTO.getEstacaoId())).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma estação de recarga", description = "Atualiza as informações de uma estação de recarga existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estação de recarga atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Estação de recarga não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<EstacaoRecargaDTO> atualizarEstacaoRecarga(
            @Parameter(description = "ID da estação de recarga a ser atualizada") @PathVariable Long id,
            @Parameter(description = "Dados atualizados da estação de recarga") @Valid @RequestBody EstacaoRecargaCreateDTO estacaoCreateDTO) {
        EstacaoRecargaDTO estacaoDTO = estacaoRecargaService.atualizarEstacaoRecarga(id, estacaoCreateDTO);
        return EntityModel.of(estacaoDTO,
                linkTo(methodOn(EstacaoRecargaController.class).obterEstacaoRecarga(id)).withSelfRel(),
                linkTo(methodOn(EstacaoRecargaController.class).listarTodos(0, 10)).withRel(LISTAR_TODOS)); // Substituído literal por constante
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar uma estação de recarga", description = "Remove uma estação de recarga fornecendo o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estação de recarga deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estação de recarga não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public void deletarEstacaoRecarga(
            @Parameter(description = "ID da estação de recarga a ser deletada") @PathVariable Long id) {
        estacaoRecargaService.deletarEstacaoRecarga(id);
    }
}