package controller;

import dto.Bairro.BairroCreateDTO;
import dto.Bairro.BairroDTO;
import service.BairroService;
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
@RequestMapping("/bairros")
public class BairroController {

    private final BairroService bairroService;

    @Autowired
    public BairroController(BairroService bairroService) {
        this.bairroService = bairroService;
    }

    @GetMapping
    public CollectionModel<EntityModel<BairroDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BairroDTO> bairrosPaginados = bairroService.listarTodosPaginado(pageable);

        List<EntityModel<BairroDTO>> bairros = bairrosPaginados.getContent().stream()
                .map(bairro -> EntityModel.of(bairro,
                        linkTo(methodOn(BairroController.class).obterBairro(bairro.getBairroId())).withSelfRel(),
                        linkTo(methodOn(BairroController.class).listarTodos(page, size)).withRel("bairros")))
                .collect(Collectors.toList());

        return CollectionModel.of(bairros, linkTo(methodOn(BairroController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<BairroDTO> obterBairro(@PathVariable Long id) {
        BairroDTO bairroDTO = bairroService.obterPorId(id);
        return EntityModel.of(bairroDTO,
                linkTo(methodOn(BairroController.class).obterBairro(id)).withSelfRel(),
                linkTo(methodOn(BairroController.class).listarTodos(0, 10)).withRel("bairros"));
    }

    @PostMapping
    public EntityModel<BairroDTO> criarBairro(@RequestBody BairroCreateDTO bairroCreateDTO) {
        BairroDTO bairroDTO = bairroService.criarBairro(bairroCreateDTO);
        return EntityModel.of(bairroDTO,
                linkTo(methodOn(BairroController.class).obterBairro(bairroDTO.getBairroId())).withSelfRel(),
                linkTo(methodOn(BairroController.class).listarTodos(0, 10)).withRel("bairros"));
    }

    @PutMapping("/{id}")
    public EntityModel<BairroDTO> atualizarBairro(@PathVariable Long id, @RequestBody BairroCreateDTO bairroCreateDTO) {
        BairroDTO bairroDTO = bairroService.atualizarBairro(id, bairroCreateDTO);
        return EntityModel.of(bairroDTO,
                linkTo(methodOn(BairroController.class).obterBairro(id)).withSelfRel(),
                linkTo(methodOn(BairroController.class).listarTodos(0, 10)).withRel("bairros"));
    }

    @DeleteMapping("/{id}")
    public EntityModel<Void> deletarBairro(@PathVariable Long id) {
        bairroService.deletarBairro(id);
        return EntityModel.of(null,
                linkTo(methodOn(BairroController.class).listarTodos(0, 10)).withRel("bairros"));
    }
}
