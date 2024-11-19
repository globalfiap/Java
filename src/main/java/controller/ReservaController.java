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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping(value = "/reservas", produces = "application/json", consumes = "application/json")
@Tag(name = "Reservas", description = "Reserva Controller")
public class ReservaController {

    private final ReservaService reservaService;

    // Constante para evitar literais duplicados
    private static final String LISTAR_RESERVAS = "reservas";

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as reservas", description = "Retorna uma lista paginada de todas as reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public CollectionModel<EntityModel<ReservaDTO>> listarTodas(
            @Parameter(description = "Página a ser exibida") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de elementos por página") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservaDTO> reservasPaginadas = reservaService.listarTodasPaginado(pageable);

        // Substituição de .collect(Collectors.toList()) por .toList()
        List<EntityModel<ReservaDTO>> reservas = reservasPaginadas.getContent().stream()
                .map(reservaDTO -> EntityModel.of(reservaDTO,
                        linkTo(methodOn(ReservaController.class).obterReserva(reservaDTO.getReservaId())).withSelfRel()))
                .toList();

        return CollectionModel.of(reservas, linkTo(methodOn(ReservaController.class).listarTodas(page, size)).withSelfRel());
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Obter uma reserva específica", description = "Retorna os detalhes de uma reserva pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<ReservaDTO> obterReserva(
            @Parameter(description = "ID da reserva a ser obtida") @PathVariable Long id) {
        ReservaDTO reservaDTO = reservaService.obterPorId(id);

        return EntityModel.of(reservaDTO,
                linkTo(methodOn(ReservaController.class).obterReserva(id)).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listarTodas(0, 10)).withRel(LISTAR_RESERVAS));
    }

    @PostMapping
    @Operation(summary = "Criar uma nova reserva", description = "Cria uma nova reserva com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<ReservaDTO> criarReserva(
            @Parameter(description = "Dados da reserva a ser criada") @Valid @RequestBody ReservaCreateDTO reservaCreateDTO) {
        ReservaDTO reservaDTO = reservaService.criarReserva(reservaCreateDTO);

        return EntityModel.of(reservaDTO,
                linkTo(methodOn(ReservaController.class).obterReserva(reservaDTO.getReservaId())).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listarTodas(0, 10)).withRel(LISTAR_RESERVAS));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualizar uma reserva", description = "Atualiza as informações de uma reserva existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public EntityModel<ReservaDTO> atualizarReserva(
            @Parameter(description = "ID da reserva a ser atualizada") @PathVariable Long id,
            @Parameter(description = "Dados atualizados da reserva") @Valid @RequestBody ReservaCreateDTO reservaCreateDTO) {
        ReservaDTO reservaDTO = reservaService.atualizarReserva(id, reservaCreateDTO);

        return EntityModel.of(reservaDTO,
                linkTo(methodOn(ReservaController.class).obterReserva(id)).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listarTodas(0, 10)).withRel(LISTAR_RESERVAS));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletar uma reserva", description = "Remove uma reserva pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deletarReserva(
            @Parameter(description = "ID da reserva a ser deletada") @PathVariable Long id) {
        reservaService.deletarReserva(id);
        return ResponseEntity.noContent().build();
    }
}