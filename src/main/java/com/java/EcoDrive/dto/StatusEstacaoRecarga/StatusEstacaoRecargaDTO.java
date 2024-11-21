package com.java.EcoDrive.dto.StatusEstacaoRecarga;

import java.time.LocalDateTime;

public class StatusEstacaoRecargaDTO {

    private Long statusId;
    private Long estacaoId;
    private String status;
    private LocalDateTime ultimaAtualizacao;

    // Getters e Setters

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getEstacaoId() {
        return estacaoId;
    }

    public void setEstacaoId(Long estacaoId) {
        this.estacaoId = estacaoId;
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
