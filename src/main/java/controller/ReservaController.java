package controller;

import dto.Reserva.ReservaCreateDTO;
import dto.Reserva.ReservaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import service.ReservaService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/reservas")
@Tag(name = "Reservas", description = "Reserva Controller")
public class ReservaController {

    private final ReservaService reservaService;

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as reservas", description = "Retorna uma lista paginada de todas as reservas")
    public CollectionModel<EntityModel<ReservaDTO>> listarTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservaDTO> reservasPaginadas = reservaService.listarTodasPaginado(pageable);

        List<EntityModel<ReservaDTO>> reservas = reservasPaginadas.getContent().stream()
                .map(reservaDTO -> EntityModel.of(reservaDTO,
                        linkTo(methodOn(ReservaController.class).obterReserva(reservaDTO.getReservaId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(reservas, linkTo(methodOn(ReservaController.class).listarTodas(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma reserva específica", description = "Retorna os detalhes de uma reserva pelo seu ID")
    public EntityModel<ReservaDTO> obterReserva(@PathVariable Long id) {
        ReservaDTO reservaDTO = reservaService.obterPorId(id);

        return EntityModel.of(reservaDTO,
                linkTo(methodOn(ReservaController.class).obterReserva(id)).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listarTodas(0, 10)).withRel("reservas"));
    }

    @PostMapping
    @Operation(summary = "Criar uma nova reserva", description = "Cria uma nova reserva com os dados fornecidos")
    public EntityModel<ReservaDTO> criarReserva(@Valid @RequestBody ReservaCreateDTO reservaCreateDTO) {
        ReservaDTO reservaDTO = reservaService.criarReserva(reservaCreateDTO);

        return EntityModel.of(reservaDTO,
                linkTo(methodOn(ReservaController.class).obterReserva(reservaDTO.getReservaId())).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listarTodas(0, 10)).withRel("reservas"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma reserva", description = "Atualiza as informações de uma reserva existente pelo seu ID")
    public EntityModel<ReservaDTO> atualizarReserva(@PathVariable Long id, @Valid @RequestBody ReservaCreateDTO reservaCreateDTO) {
        ReservaDTO reservaDTO = reservaService.atualizarReserva(id, reservaCreateDTO);

        return EntityModel.of(reservaDTO,
                linkTo(methodOn(ReservaController.class).obterReserva(id)).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listarTodas(0, 10)).withRel("reservas"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma reserva", description = "Remove uma reserva pelo seu ID")
    public ResponseEntity<Void> deletarReserva(@PathVariable Long id) {
        reservaService.deletarReserva(id);
        return ResponseEntity.noContent().build();
    }
}
