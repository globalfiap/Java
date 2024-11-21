package com.java.EcoDrive.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "fonte_energia")
public class FonteEnergia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fonte_id")
    private Long fonteId;

    @Column(name = "tipo_energia", nullable = false, length = 50)
    private String tipoEnergia;

    // Relacionamentos
    @OneToMany(mappedBy = "fonteEnergia")
    private Set<EstacaoSustentavel> estacoesSustentaveis;

    // Getters e Setters

    public Long getFonteId() {
        return fonteId;
    }

    public void setFonteId(Long fonteId) {
        this.fonteId = fonteId;
    }

    public String getTipoEnergia() {
        return tipoEnergia;
    }

    public void setTipoEnergia(String tipoEnergia) {
        this.tipoEnergia = tipoEnergia;
    }

    public Set<EstacaoSustentavel> getEstacoesSustentaveis() {
        return estacoesSustentaveis;
    }

    public void setEstacoesSustentaveis(Set<EstacaoSustentavel> estacoesSustentaveis) {
        this.estacoesSustentaveis = estacoesSustentaveis;
    }
}
