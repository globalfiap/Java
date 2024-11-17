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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/concessionarias")
public class ConcessionariaController {

    private final ConcessionariaService concessionariaService;

    @Autowired
    public ConcessionariaController(ConcessionariaService concessionariaService) {
        this.concessionariaService = concessionariaService;
    }

    @GetMapping
    public CollectionModel<EntityModel<ConcessionariaDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ConcessionariaDTO> concessionariasPaginadas = concessionariaService.listarTodosPaginado(pageable);

        List<EntityModel<ConcessionariaDTO>> concessionarias = concessionariasPaginadas.getContent().stream()
                .map(concessionaria -> EntityModel.of(concessionaria,
                        linkTo(methodOn(ConcessionariaController.class).obterConcessionaria(concessionaria.getConcessionariaId())).withSelfRel(),
                        linkTo(methodOn(ConcessionariaController.class).listarTodos(page, size)).withRel("concessionarias")))
                .collect(Collectors.toList());

        return CollectionModel.of(concessionarias, linkTo(methodOn(ConcessionariaController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<ConcessionariaDTO> obterConcessionaria(@PathVariable Long id) {
        ConcessionariaDTO concessionariaDTO = concessionariaService.obterPorId(id);
        return EntityModel.of(concessionariaDTO,
                linkTo(methodOn(ConcessionariaController.class).obterConcessionaria(id)).withSelfRel(),
                linkTo(methodOn(ConcessionariaController.class).listarTodos(0, 10)).withRel("concessionarias"));
    }

    @PostMapping
    public EntityModel<ConcessionariaDTO> criarConcessionaria(@RequestBody ConcessionariaCreateDTO concessionariaCreateDTO) {
        ConcessionariaDTO concessionariaDTO = concessionariaService.criarConcessionaria(concessionariaCreateDTO);
        return EntityModel.of(concessionariaDTO,
                linkTo(methodOn(ConcessionariaController.class).obterConcessionaria(concessionariaDTO.getConcessionariaId())).withSelfRel(),
                linkTo(methodOn(ConcessionariaController.class).listarTodos(0, 10)).withRel("concessionarias"));
    }

    @PutMapping("/{id}")
    public EntityModel<ConcessionariaDTO> atualizarConcessionaria(@PathVariable Long id, @RequestBody ConcessionariaCreateDTO concessionariaCreateDTO) {
        ConcessionariaDTO concessionariaDTO = concessionariaService.atualizarConcessionaria(id, concessionariaCreateDTO);
        return EntityModel.of(concessionariaDTO,
                linkTo(methodOn(ConcessionariaController.class).obterConcessionaria(id)).withSelfRel(),
                linkTo(methodOn(ConcessionariaController.class).listarTodos(0, 10)).withRel("concessionarias"));
    }

    @DeleteMapping("/{id}")
    public EntityModel<Void> deletarConcessionaria(@PathVariable Long id) {
        concessionariaService.deletarConcessionaria(id);
        return EntityModel.of(null,
                linkTo(methodOn(ConcessionariaController.class).listarTodos(0, 10)).withRel("concessionarias"));
    }
}
