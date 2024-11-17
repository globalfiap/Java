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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/historico-carregamento")
@Api(value = "Histórico de Carregamento Controller", tags = {"Histórico de Carregamento"})
public class HistoricoCarregamentoController {

    private final HistoricoCarregamentoService historicoCarregamentoService;

    @Autowired
    public HistoricoCarregamentoController(HistoricoCarregamentoService historicoCarregamentoService) {
        this.historicoCarregamentoService = historicoCarregamentoService;
    }

    @GetMapping
    @ApiOperation(value = "Listar todos os históricos de carregamento", notes = "Retorna uma lista paginada de todos os históricos de carregamento disponíveis")
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
    @ApiOperation(value = "Obter um histórico de carregamento específico", notes = "Retorna os detalhes de um histórico de carregamento pelo seu ID")
    public EntityModel<HistoricoCarregamentoDTO> obterHistorico(@PathVariable Long id) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.obterPorId(id);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(id)).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel("historico-carregamento"));
    }

    @PostMapping
    @ApiOperation(value = "Criar um novo histórico de carregamento", notes = "Cria um novo histórico de carregamento com os dados fornecidos")
    public EntityModel<HistoricoCarregamentoDTO> criarHistoricoCarregamento(@RequestBody HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.criarHistorico(historicoCreateDTO);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(historicoDTO.getHistoricoId())).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel("historico-carregamento"));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualizar um histórico de carregamento", notes = "Atualiza as informações de um histórico de carregamento existente pelo seu ID")
    public EntityModel<HistoricoCarregamentoDTO> atualizarHistorico(@PathVariable Long id, @RequestBody HistoricoCarregamentoCreateDTO historicoCreateDTO) {
        HistoricoCarregamentoDTO historicoDTO = historicoCarregamentoService.atualizarHistorico(id, historicoCreateDTO);
        return EntityModel.of(historicoDTO,
                linkTo(methodOn(HistoricoCarregamentoController.class).obterHistorico(id)).withSelfRel(),
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel("historico-carregamento"));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletar um histórico de carregamento", notes = "Remove um histórico de carregamento pelo seu ID")
    public EntityModel<Void> deletarHistorico(@PathVariable Long id) {
        historicoCarregamentoService.deletarHistorico(id);
        return EntityModel.of(null,
                linkTo(methodOn(HistoricoCarregamentoController.class).listarTodos(0, 10)).withRel("historico-carregamento"));
    }
}

