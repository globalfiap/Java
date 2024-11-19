package controller;

import dto.FonteEnergia.FonteEnergiaCreateDTO;
import dto.FonteEnergia.FonteEnergiaDTO;
import org.springframework.http.ResponseEntity;
import service.FonteEnergiaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(value = "/fontes-energia", produces = "application/json", consumes = "application/json")
@Tag(name = "Fontes de Energia", description = "Fonte de Energia Controller")
public class FonteEnergiaController {

    private final FonteEnergiaService fonteEnergiaService;

    @Autowired
    public FonteEnergiaController(FonteEnergiaService fonteEnergiaService) {
        this.fonteEnergiaService = fonteEnergiaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as fontes de energia", description = "Retorna uma lista paginada de todas as fontes de energia disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de fontes de energia retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public CollectionModel<EntityModel<FonteEnergiaDTO>> listarTodas(
            @Parameter(description = "Página a ser exibida") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de elementos por página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FonteEnergiaDTO> fontesPaginadas = fonteEnergiaService.listarTodasPaginado(pageable);

        List<EntityModel<FonteEnergiaDTO>> fontes = fontesPaginadas.getContent().stream()
                .map(fonte -> EntityModel.of(fonte,
                        linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(fonte.getFonteId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(fontes, linkTo(methodOn(FonteEnergiaController.class).listarTodas(page, size)).withSelfRel());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Obter uma fonte de energia específica", description = "Retorna os detalhes de uma fonte de energia pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fonte de energia encontrada"),
            @ApiResponse(responseCode = "404", description = "Fonte de energia não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<FonteEnergiaDTO> obterFonteEnergia(
            @Parameter(description = "ID da fonte de energia a ser obtida") @PathVariable Long id) {
        FonteEnergiaDTO fonteDTO = fonteEnergiaService.obterPorId(id);
        return EntityModel.of(fonteDTO,
                linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(id)).withSelfRel(),
                linkTo(methodOn(FonteEnergiaController.class).listarTodas(0, 10)).withRel("listarTodas"));
    }

    @PostMapping
    @Operation(summary = "Criar uma nova fonte de energia", description = "Cria uma nova fonte de energia com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fonte de energia criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<FonteEnergiaDTO> criarFonteEnergia(
            @Parameter(description = "Dados da fonte de energia a ser criada") @Valid @RequestBody FonteEnergiaCreateDTO fonteCreateDTO) {
        FonteEnergiaDTO fonteDTO = fonteEnergiaService.criarFonteEnergia(fonteCreateDTO);
        return EntityModel.of(fonteDTO,
                linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(fonteDTO.getFonteId())).withSelfRel(),
                linkTo(methodOn(FonteEnergiaController.class).listarTodas(0, 10)).withRel("listarTodas"));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualizar uma fonte de energia", description = "Atualiza as informações de uma fonte de energia existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fonte de energia atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Fonte de energia não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<FonteEnergiaDTO> atualizarFonteEnergia(
            @Parameter(description = "ID da fonte de energia a ser atualizada") @PathVariable Long id,
            @Parameter(description = "Dados atualizados da fonte de energia") @Valid @RequestBody FonteEnergiaCreateDTO fonteCreateDTO) {
        FonteEnergiaDTO fonteDTO = fonteEnergiaService.atualizarFonteEnergia(id, fonteCreateDTO);
        return EntityModel.of(fonteDTO,
                linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(id)).withSelfRel(),
                linkTo(methodOn(FonteEnergiaController.class).listarTodas(0, 10)).withRel("listarTodas"));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletar uma fonte de energia", description = "Remove uma fonte de energia pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fonte de energia deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fonte de energia não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletarFonteEnergia(
            @Parameter(description = "ID da fonte de energia a ser deletada") @PathVariable Long id) {
        fonteEnergiaService.deletarFonteEnergia(id);
        return ResponseEntity.noContent().build();
    }
}
