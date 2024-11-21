package com.java.EcoDrive.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "status_estacao_recarga")
public class StatusEstacaoRecarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @ManyToOne
    @JoinColumn(name = "estacao_id", nullable = false)
    private EstacaoRecarga estacaoRecarga;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "ultima_atualizacao", nullable = false)
    private LocalDateTime ultimaAtualizacao;

    // Getters e Setters

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public EstacaoRecarga getEstacaoRecarga() {
        return estacaoRecarga;
    }

    public void setEstacaoRecarga(EstacaoRecarga estacaoRecarga) {
        this.estacaoRecarga = estacaoRecarga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}
