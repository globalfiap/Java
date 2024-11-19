package controller;

import dto.HistoricoCarregamento.HistoricoCarregamentoCreateDTO;
import dto.HistoricoCarregamento.HistoricoCarregamentoDTO;
import org.springframework.http.ResponseEntity;
import service.HistoricoCarregamentoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/historico-carregamento")
@Tag(name = "Histórico de Carregamento", description = "Histórico de Carregamento Controller")
public class HistoricoCarregamentoController {

    private final HistoricoCarregamentoService historicoCarregamentoService;

    @Autowired
    public HistoricoCarregamentoController(HistoricoCarregamentoService historicoCarregamentoService) {
        this.historicoCarregamentoService = historicoCarregamentoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os históricos de carregamento", description = "Retorna uma lista paginada de todos os históricos de carregamento disponíveis")
    public CollectionModel<EntityModel<HistoricoCarregamentoDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoricoCarregamentoDTO> historicosPaginados = historicoCarregamentoService.listarTodosPaginado(pageable);

        List<EntityModel<HistoricoCarregamentoDTO>> historicos = historicosPaginados.getContent().stream()
                .map(historico -> EntityModel.of(historico,
                        linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(historico.getHistoricoId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(historicos, linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter um histórico de carregamento específico", description = "Retorna os detalhes de um histórico de carregamento pelo seu ID")
    public EntityModel<HistoricoCarregamentoDTO> obterHistorico(@PathVariable Long id) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.obterPorId(id);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(id)).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PostMapping
    @Operation(summary = "Criar um novo histórico de carregamento", description = "Cria um novo histórico de carregamento com os dados fornecidos")
    public EntityModel<HistoricoCarregamentoDTO> criarHistoricoCarregamento(@RequestBody HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.criarHistorico(historicoCreateDTO);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(historicoDTO.getHistoricoId())).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um histórico de carregamento", description = "Atualiza as informações de um histórico de carregamento existente pelo seu ID")
    public EntityModel<HistoricoCarregamentoDTO> atualizarHistorico(@PathVariable Long id, @RequestBody HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.atualizarHistorico(id, historicoCreateDTO);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(id)).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel("listarTodos"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um histórico de carregamento", description = "Remove um histórico de carregamento pelo seu ID")
    public ResponseEntity<Void> deletarHistorico(@PathVariable Long id) {
        historicoCarregamentoService.deletarHistorico(id);
        return ResponseEntity.noContent().build();
    }
}
