package com.java.EcoDrive.dto.Reserva;

import java.time.LocalDateTime;

public class ReservaDTO {

    private Long reservaId;
    private Long usuarioId;
    private Long estacaoId;
    private LocalDateTime dataReserva;
    private Integer status;

    // Getters e Setters

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

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
