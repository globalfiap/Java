package com.java.EcoDrive.dto.GastoCarregamento;

public class GastoCarregamentoDTO {

    private Long gastoId;
    private Long historicoId;
    private Double custoTotal;

    // Getters e Setters

    public Long getGastoId() {
        return gastoId;
    }

    public void setGastoId(Long gastoId) {
        this.gastoId = gastoId;
    }

    public Long getHistoricoId() {
        return historicoId;
    }

    public void setHistoricoId(Long historicoId) {
        this.historicoId = historicoId;
    }

    public Double getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(Double custoTotal) {
        this.custoTotal = custoTotal;
    }
}
