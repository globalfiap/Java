package controller;

import dto.Reserva.ReservaCreateDTO;
import dto.Reserva.ReservaDTO;
import service.ReservaService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/reservas")
@Api(value = "Reserva Controller", tags = {"Reservas"})
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    @ApiOperation(value = "Listar todas as reservas", notes = "Retorna uma lista paginada de todas as reservas")
    public CollectionModel<EntityModel<ReservaDTO>> listarTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservaDTO> reservasPaginadas = reservaService.listarTodasPaginado(pageable);

        List<EntityModel<ReservaDTO>> reservas = reservasPaginadas.getContent().stream()
                .map(reservaDTO -> EntityModel.of(reservaDTO,
                        linkTo(methodOn(ReservaController.class).obterReserva(reservaDTO.getReservaId())).withSelfRel(),
                        linkTo(methodOn(ReservaController.class).listarTodas(page, size)).withRel("reservas")))
                .collect(Collectors.toList());

        return CollectionModel.of(reservas, linkTo(methodOn(ReservaController.class).listarTodas(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obter uma reserva específica", notes = "Retorna os detalhes de uma reserva pelo seu ID")
    public EntityModel<ReservaDTO> obterReserva(@PathVariable Long id) {
        ReservaDTO reservaDTO = reservaService.obterPorId(id);

        return EntityModel.of(reservaDTO,
                linkTo(methodOn(ReservaController.class).obterReserva(id)).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listarTodas(0, 10)).withRel("reservas"),
                linkTo(methodOn(ReservaController.class).deletarReserva(id)).withRel("delete"),
                linkTo(methodOn(ReservaController.class).atualizarReserva(id, null)).withRel("update"));
    }

    @PostMapping
    @ApiOperation(value = "Criar uma nova reserva", notes = "Cria uma nova reserva com os dados fornecidos")
    public EntityModel<ReservaDTO> criarReserva(@Valid @RequestBody ReservaCreateDTO reservaCreateDTO) {
        ReservaDTO reservaDTO = reservaService.criarReserva(reservaCreateDTO);

        return EntityModel.of(reservaDTO,
                linkTo(methodOn(ReservaController.class).obterReserva(reservaDTO.getReservaId())).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listarTodas(0, 10)).withRel("reservas"));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualizar uma reserva", notes = "Atualiza as informações de uma reserva existente pelo seu ID")
    public EntityModel<ReservaDTO> atualizarReserva(@PathVariable Long id, @Valid @RequestBody ReservaCreateDTO reservaCreateDTO) {
        ReservaDTO reservaDTO = reservaService.atualizarReserva(id, reservaCreateDTO);

        return EntityModel.of(reservaDTO,
                linkTo(methodOn(ReservaController.class).obterReserva(id)).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listarTodas(0, 10)).withRel("reservas"));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletar uma reserva", notes = "Remove uma reserva pelo seu ID")
    public EntityModel<Void> deletarReserva(@PathVariable Long id) {
        reservaService.deletarReserva(id);

        return EntityModel.of(null,
                linkTo(methodOn(ReservaController.class).listarTodas(0, 10)).withRel("reservas"));
    }
}
