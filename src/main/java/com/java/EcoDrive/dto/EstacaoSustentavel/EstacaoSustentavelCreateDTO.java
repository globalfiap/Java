package com.java.EcoDrive.dto.EstacaoSustentavel;

import jakarta.validation.constraints.NotNull;

public class EstacaoSustentavelCreateDTO {

    @NotNull(message = "O ID da estação é obrigatório")
    private Long estacaoId;

    @NotNull(message = "O ID da fonte de energia é obrigatório")
    private Long fonteId;

    @NotNull(message = "A redução de carbono é obrigatória")
    private Double reducaoCarbono;

    // Getters e Setters

    public Long getEstacaoId() {
        return estacaoId;
    }

    public void setEstacaoId(Long estacaoId) {
        this.estacaoId = estacaoId;
    }

    public Long getFonteId() {
        return fonteId;
    }

    public void setFonteId(Long fonteId) {
        this.fonteId = fonteId;
    }

    public Double getReducaoCarbono() {
        return reducaoCarbono;
    }

    public void setReducaoCarbono(Double reducaoCarbono) {
        this.reducaoCarbono = reducaoCarbono;
    }
}
