package com.java.EcoDrive.dto.GastoCarregamento;

import jakarta.validation.constraints.NotNull;

public class GastoCarregamentoCreateDTO {

    @NotNull(message = "O ID do histórico é obrigatório")
    private Long historicoId;

    @NotNull(message = "O custo total é obrigatório")
    private Double custoTotal;

    // Getters e Setters

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
