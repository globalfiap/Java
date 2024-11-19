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
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/concessionarias")
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
    public CollectionModel<EntityModel<ConcessionariaDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
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

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma concessionária específica", description = "Retorna os detalhes de uma concessionária fornecendo o ID")
    public EntityModel<ConcessionariaDTO> obterConcessionaria(@PathVariable Long id) {
        ConcessionariaDTO concessionariaDTO = concessionariaService.obterPorId(id);
        return EntityModel.of(concessionariaDTO,
                linkTo(methodOn(ConcessionariaController.class).obterConcessionaria(id)).withSelfRel(),
                linkTo(methodOn(ConcessionariaController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar uma nova concessionária", description = "Cria uma nova concessionária com as informações fornecidas")
    public EntityModel<ConcessionariaDTO> criarConcessionaria(@Valid @RequestBody ConcessionariaCreateDTO concessionariaCreateDTO) {
        ConcessionariaDTO concessionariaDTO = concessionariaService.criarConcessionaria(concessionariaCreateDTO);
        return EntityModel.of(concessionariaDTO,
                linkTo(methodOn(ConcessionariaController.class).obterConcessionaria(concessionariaDTO.getConcessionariaId())).withSelfRel(),
                linkTo(methodOn(ConcessionariaController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma concessionária", description = "Atualiza as informações de uma concessionária existente")
    public EntityModel<ConcessionariaDTO> atualizarConcessionaria(@PathVariable Long id, @Valid @RequestBody ConcessionariaCreateDTO concessionariaCreateDTO) {
        ConcessionariaDTO concessionariaDTO = concessionariaService.atualizarConcessionaria(id, concessionariaCreateDTO);
        return EntityModel.of(concessionariaDTO,
                linkTo(methodOn(ConcessionariaController.class).obterConcessionaria(id)).withSelfRel(),
                linkTo(methodOn(ConcessionariaController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar uma concessionária", description = "Remove uma concessionária fornecendo o ID")
    public void deletarConcessionaria(@PathVariable Long id) {
        concessionariaService.deletarConcessionaria(id);
    }
}
