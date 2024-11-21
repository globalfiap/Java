package com.java.EcoDrive.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estacao_sustentavel")
public class EstacaoSustentavel {

    @Id
    @Column(name = "estacao_id")
    private Long estacaoId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "estacao_id")
    private EstacaoRecarga estacaoRecarga;

    @ManyToOne
    @JoinColumn(name = "fonte_id", nullable = false)
    private FonteEnergia fonteEnergia;

    @Column(name = "reducao_carbono", nullable = false, precision = 10)
    private Double reducaoCarbono;

    // Getters e Setters

    public Long getEstacaoId() {
        return estacaoId;
    }

    public void setEstacaoId(Long estacaoId) {
        this.estacaoId = estacaoId;
    }

    public EstacaoRecarga getEstacaoRecarga() {
        return estacaoRecarga;
    }

    public void setEstacaoRecarga(EstacaoRecarga estacaoRecarga) {
        this.estacaoRecarga = estacaoRecarga;
    }

    public FonteEnergia getFonteEnergia() {
        return fonteEnergia;
    }

    public void setFonteEnergia(FonteEnergia fonteEnergia) {
        this.fonteEnergia = fonteEnergia;
    }

    public Double getReducaoCarbono() {
        return reducaoCarbono;
    }

    public void setReducaoCarbono(Double reducaoCarbono) {
        this.reducaoCarbono = reducaoCarbono;
    }
}
