package controller;

import dto.HistoricoCarregamento.HistoricoCarregamentoCreateDTO;
import dto.HistoricoCarregamento.HistoricoCarregamentoDTO;
import service.HistoricoCarregamentoService;
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
@RequestMapping("/historico-carregamento")
public class HistoricoCarregamentoController {

    private final HistoricoCarregamentoService historicoCarregamentoService;

    @Autowired
    public HistoricoCarregamentoController(HistoricoCarregamentoService historicoCarregamentoService) {
        this.historicoCarregamentoService = historicoCarregamentoService;
    }

    @GetMapping
    public CollectionModel<EntityModel<HistoricoCarregamentoDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoricoCarregamentoDTO> historicosPaginados = historicoCarregamentoService.listarTodosPaginado(pageable);

        List<EntityModel<HistoricoCarregamentoDTO>> historicos = historicosPaginados.getContent().stream()
                .map(historico -> EntityModel.of(historico,
                        linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(historico.getHistoricoId())).withSelfRel(),
                        linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(page, size)).withRel("historico-carregamento")))
                .collect(Collectors.toList());

        return CollectionModel.of(historicos, linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<HistoricoCarregamentoDTO> obterHistorico(@PathVariable Long id) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.obterPorId(id);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(id)).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel("historico-carregamento"));
    }

    @PostMapping
    public EntityModel<HistoricoCarregamentoDTO> criarHistoricoCarregamento(@RequestBody HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.criarHistorico(historicoCreateDTO);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(historicoDTO.getHistoricoId())).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel("historico-carregamento"));
    }

    @PutMapping("/{id}")
    public EntityModel<HistoricoCarregamentoDTO> atualizarHistorico(@PathVariable Long id, @RequestBody HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.atualizarHistorico(id, historicoCreateDTO);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(id)).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel("historico-carregamento"));
    }

    @DeleteMapping("/{id}")
    public EntityModel<Void> deletarHistorico(@PathVariable Long id) {
        historicoCarregamentoService.deletarHistorico(id);
        return EntityModel.of(null,
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel("historico-carregamento"));
    }
}
