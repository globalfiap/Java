package com.java.EcoDrive.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "gasto_carregamento")
public class GastoCarregamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gasto_id")
    private Long gastoId;

    @OneToOne
    @JoinColumn(name = "historico_id", nullable = false)
    private HistoricoCarregamento historicoCarregamento;

    @Column(name = "data_gasto", nullable = false)
    private LocalDateTime dataGasto;

    @Column(name = "custo_total", nullable = false, precision = 10)
    private Double custoTotal;

    // Getters e Setters

    public Long getGastoId() {
        return gastoId;
    }

    public void setGastoId(Long gastoId) {
        this.gastoId = gastoId;
    }

    public HistoricoCarregamento getHistoricoCarregamento() {
        return historicoCarregamento;
    }

    public void setHistoricoCarregamento(HistoricoCarregamento historicoCarregamento) {
        this.historicoCarregamento = historicoCarregamento;
    }

    public Double getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(Double custoTotal) {
        this.custoTotal = custoTotal;
    }
}
