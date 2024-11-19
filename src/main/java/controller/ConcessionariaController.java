package controller;

import dto.Concessionaria.ConcessionariaCreateDTO;
import dto.Concessionaria.ConcessionariaDTO;
import service.ConcessionariaService;
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
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping(value = "/concessionarias", produces = "application/json", consumes = "application/json")
@Tag(name = "Concessionárias", description = "Controle das Concessionárias")
@Validated
public class ConcessionariaController {

    private final ConcessionariaService concessionariaService;

    @Autowired
    public ConcessionariaController(ConcessionariaService concessionariaService) {
        this.concessionariaService = concessionariaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as concessionárias", description = "Retorna uma lista paginada de todas as concessionárias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de concessionárias retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public CollectionModel<EntityModel<ConcessionariaDTO>> listarTodos(
            @Parameter(description = "Página a ser exibida") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de elementos por página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ConcessionariaDTO> concessionariasPaginadas = concessionariaService.listarTodosPaginado(pageable);

        List<EntityModel<ConcessionariaDTO>> concessionarias = concessionariasPaginadas.getContent().stream()
                .map(concessionaria -> EntityModel.of(concessionaria,
                        linkTo(methodOn(ConcessionariaController.class).obterConcessionaria(concessionaria.getConcessionariaId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(concessionarias,
                linkTo(methodOn(ConcessionariaController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Obter uma concessionária específica", description = "Retorna os detalhes de uma concessionária fornecendo o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concessionária encontrada"),
            @ApiResponse(responseCode = "404", description = "Concessionária não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<ConcessionariaDTO> obterConcessionaria(
            @Parameter(description = "ID da concessionária a ser obtida") @PathVariable Long id) {
        ConcessionariaDTO concessionariaDTO = concessionariaService.obterPorId(id);
        return EntityModel.of(concessionariaDTO,
                linkTo(methodOn(ConcessionariaController.class).obterConcessionaria(id)).withSelfRel(),
                linkTo(methodOn(ConcessionariaController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar uma nova concessionária", description = "Cria uma nova concessionária com as informações fornecidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Concessionária criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<ConcessionariaDTO> criarConcessionaria(
            @Parameter(description = "Dados da concessionária a ser criada") @Valid @RequestBody ConcessionariaCreateDTO concessionariaCreateDTO) {
        ConcessionariaDTO concessionariaDTO = concessionariaService.criarConcessionaria(concessionariaCreateDTO);
        return EntityModel.of(concessionariaDTO,
                linkTo(methodOn(ConcessionariaController.class).obterConcessionaria(concessionariaDTO.getConcessionariaId())).withSelfRel(),
                linkTo(methodOn(ConcessionariaController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualizar uma concessionária", description = "Atualiza as informações de uma concessionária existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concessionária atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Concessionária não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<ConcessionariaDTO> atualizarConcessionaria(
            @Parameter(description = "ID da concessionária a ser atualizada") @PathVariable Long id,
            @Parameter(description = "Dados atualizados da concessionária") @Valid @RequestBody ConcessionariaCreateDTO concessionariaCreateDTO) {
        ConcessionariaDTO concessionariaDTO = concessionariaService.atualizarConcessionaria(id, concessionariaCreateDTO);
        return EntityModel.of(concessionariaDTO,
                linkTo(methodOn(ConcessionariaController.class).obterConcessionaria(id)).withSelfRel(),
                linkTo(methodOn(ConcessionariaController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar uma concessionária", description = "Remove uma concessionária fornecendo o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Concessionária deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Concessionária não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public void deletarConcessionaria(
            @Parameter(description = "ID da concessionária a ser deletada") @PathVariable Long id) {
        concessionariaService.deletarConcessionaria(id);
    }
}