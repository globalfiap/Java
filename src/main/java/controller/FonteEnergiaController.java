package controller;

import dto.FonteEnergia.FonteEnergiaCreateDTO;
import dto.FonteEnergia.FonteEnergiaDTO;
import service.FonteEnergiaService;
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
@RequestMapping("/fontes-energia")
@Api(value = "Fonte de Energia Controller", tags = {"Fontes de Energia"})
public class FonteEnergiaController {

    private final FonteEnergiaService fonteEnergiaService;

    @Autowired
    public FonteEnergiaController(FonteEnergiaService fonteEnergiaService) {
        this.fonteEnergiaService = fonteEnergiaService;
    }

    @GetMapping
    @ApiOperation(value = "Listar todas as fontes de energia", notes = "Retorna uma lista paginada de todas as fontes de energia disponíveis")
    public CollectionModel<EntityModel<FonteEnergiaDTO>> listarTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FonteEnergiaDTO> fontesPaginadas = fonteEnergiaService.listarTodasPaginado(pageable);

        List<EntityModel<FonteEnergiaDTO>> fontes = fontesPaginadas.getContent().stream()
                .map(fonte -> EntityModel.of(fonte,
                        linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(fonte.getFonteId())).withSelfRel(),
                        linkTo(methodOn(FonteEnergiaController.class).listarTodas(page, size)).withRel("fontes-energia")))
                .collect(Collectors.toList());

        return CollectionModel.of(fontes, linkTo(methodOn(FonteEnergiaController.class).listarTodas(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obter uma fonte de energia específica", notes = "Retorna os detalhes de uma fonte de energia pelo seu ID")
    public EntityModel<FonteEnergiaDTO> obterFonteEnergia(@PathVariable Long id) {
        FonteEnergiaDTO fonteDTO = fonteEnergiaService.obterPorId(id);
        return EntityModel.of(fonteDTO,
                linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(id)).withSelfRel(),
                linkTo(methodOn(FonteEnergiaController.class).listarTodas(0, 10)).withRel("fontes-energia"));
    }

    @PostMapping
    @ApiOperation(value = "Criar uma nova fonte de energia", notes = "Cria uma nova fonte de energia com os dados fornecidos")
    public EntityModel<FonteEnergiaDTO> criarFonteEnergia(@RequestBody FonteEnergiaCreateDTO fonteCreateDTO) {
        FonteEnergiaDTO fonteDTO = fonteEnergiaService.criarFonteEnergia(fonteCreateDTO);
        return EntityModel.of(fonteDTO,
                linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(fonteDTO.getFonteId())).withSelfRel(),
                linkTo(methodOn(FonteEnergiaController.class).listarTodas(0, 10)).withRel("fontes-energia"));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualizar uma fonte de energia", notes = "Atualiza as informações de uma fonte de energia existente pelo seu ID")
    public EntityModel<FonteEnergiaDTO> atualizarFonteEnergia(@PathVariable Long id, @RequestBody FonteEnergiaCreateDTO fonteCreateDTO) {
        FonteEnergiaDTO fonteDTO = fonteEnergiaService.atualizarFonteEnergia(id, fonteCreateDTO);
        return EntityModel.of(fonteDTO,
                linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(id)).withSelfRel(),
                linkTo(methodOn(FonteEnergiaController.class).listarTodas(0, 10)).withRel("fontes-energia"));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletar uma fonte de energia", notes = "Remove uma fonte de energia pelo seu ID")
    public EntityModel<Void> deletarFonteEnergia(@PathVariable Long id) {
        fonteEnergiaService.deletarFonteEnergia(id);
        return EntityModel.of(null,
                linkTo(methodOn(FonteEnergiaController.class).listarTodas(0, 10)).withRel("fontes-energia"));
    }
}

