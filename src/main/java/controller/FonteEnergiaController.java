package controller;

import dto.FonteEnergia.FonteEnergiaCreateDTO;
import dto.FonteEnergia.FonteEnergiaDTO;
import service.FonteEnergiaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/fontes-energia")
public class FonteEnergiaController {

    private final FonteEnergiaService fonteEnergiaService;

    @Autowired
    public FonteEnergiaController(FonteEnergiaService fonteEnergiaService) {
        this.fonteEnergiaService = fonteEnergiaService;
    }

    @GetMapping
    public CollectionModel<EntityModel<FonteEnergiaDTO>> listarTodas() {
        List<EntityModel<FonteEnergiaDTO>> fontes = fonteEnergiaService.listarTodas().stream()
                .map(fonte -> EntityModel.of(fonte,
                        linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(fonte.getFonteId())).withSelfRel(),
                        linkTo(methodOn(FonteEnergiaController.class).listarTodas()).withRel("fontes-energia")))
                .collect(Collectors.toList());

        return CollectionModel.of(fontes, linkTo(methodOn(FonteEnergiaController.class).listarTodas()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<FonteEnergiaDTO> obterFonteEnergia(@PathVariable Long id) {
        FonteEnergiaDTO fonteDTO = fonteEnergiaService.obterPorId(id);
        return EntityModel.of(fonteDTO,
                linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(id)).withSelfRel(),
                linkTo(methodOn(FonteEnergiaController.class).listarTodas()).withRel("fontes-energia"));
    }

    @PostMapping
    public EntityModel<FonteEnergiaDTO> criarFonteEnergia(@RequestBody FonteEnergiaCreateDTO fonteCreateDTO) {
        FonteEnergiaDTO fonteDTO = fonteEnergiaService.criarFonteEnergia(fonteCreateDTO);
        return EntityModel.of(fonteDTO,
                linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(fonteDTO.getFonteId())).withSelfRel(),
                linkTo(methodOn(FonteEnergiaController.class).listarTodas()).withRel("fontes-energia"));
    }

    @PutMapping("/{id}")
    public EntityModel<FonteEnergiaDTO> atualizarFonteEnergia(@PathVariable Long id, @RequestBody FonteEnergiaCreateDTO fonteCreateDTO) {
        FonteEnergiaDTO fonteDTO = fonteEnergiaService.atualizarFonteEnergia(id, fonteCreateDTO);
        return EntityModel.of(fonteDTO,
                linkTo(methodOn(FonteEnergiaController.class).obterFonteEnergia(id)).withSelfRel(),
                linkTo(methodOn(FonteEnergiaController.class).listarTodas()).withRel("fontes-energia"));
    }

    @DeleteMapping("/{id}")
    public EntityModel<Void> deletarFonteEnergia(@PathVariable Long id) {
        fonteEnergiaService.deletarFonteEnergia(id);
        return EntityModel.of(null,
                linkTo(methodOn(FonteEnergiaController.class).listarTodas()).withRel("fontes-energia"));
    }
}
