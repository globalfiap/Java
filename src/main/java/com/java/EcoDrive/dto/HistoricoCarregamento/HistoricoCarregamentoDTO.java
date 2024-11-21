package com.java.EcoDrive.dto.HistoricoCarregamento;

import java.time.LocalDateTime;

public class HistoricoCarregamentoDTO {

    private Long historicoId;
    private Long usuarioId;
    private Long veiculoId;
    private Long estacaoId;
    private LocalDateTime dataCarregamento;
    private Double kwhConsumidos;

    // Getters e Setters

    public Long getHistoricoId() {
        return historicoId;
    }

    public void setHistoricoId(Long historicoId) {
        this.historicoId = historicoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }

    public Long getEstacaoId() {
        return estacaoId;
    }

    public void setEstacaoId(Long estacaoId) {
        this.estacaoId = estacaoId;
    }

    public LocalDateTime getDataCarregamento() {
        return dataCarregamento;
    }

    public void setDataCarregamento(LocalDateTime dataCarregamento) {
        this.dataCarregamento = dataCarregamento;
    }

    public Double getKwhConsumidos() {
        return kwhConsumidos;
    }

    public void setKwhConsumidos(Double kwhConsumidos) {
        this.kwhConsumidos = kwhConsumidos;
    }
}
