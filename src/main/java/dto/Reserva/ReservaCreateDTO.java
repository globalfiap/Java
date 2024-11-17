package dto.Reserva;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ReservaCreateDTO {

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "O ID da estação é obrigatório")
    private Long estacaoId;

    @NotNull(message = "A data da reserva é obrigatória")
    private LocalDateTime dataReserva;

    @NotNull(message = "O status é obrigatório")
    private Integer status;

    // Getters e Setters

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getEstacaoId() {
        return estacaoId;
    }

    public void setEstacaoId(Long estacaoId) {
        this.estacaoId = estacaoId;
    }

    public LocalDateTime getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
