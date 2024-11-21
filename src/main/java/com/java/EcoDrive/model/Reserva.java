package com.java.EcoDrive.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserva_id")
    private Long reservaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estacao_id", nullable = false)
    private EstacaoRecarga estacaoRecarga;

    @Column(name = "data_reserva", nullable = false)
    private LocalDateTime dataReserva;

    @Column(name = "status", nullable = false)
    private Integer status;

    // Construtor padrão
    public Reserva() {
    }

    // Construtor completo
    public Reserva(Usuario usuario, EstacaoRecarga estacaoRecarga, LocalDateTime dataReserva, Integer status) {
        this.usuario = usuario;
        this.estacaoRecarga = estacaoRecarga;
        this.dataReserva = dataReserva;
        this.status = status;
    }

    // Getters e Setters

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EstacaoRecarga getEstacaoRecarga() {
        return estacaoRecarga;
    }

    public void setEstacaoRecarga(EstacaoRecarga estacaoRecarga) {
        this.estacaoRecarga = estacaoRecarga;
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
