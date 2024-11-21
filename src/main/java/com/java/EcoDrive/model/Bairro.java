package com.java.EcoDrive.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "bairro")
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bairro_id")
    private Long bairroId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    // Relacionamentos
    @OneToMany(mappedBy = "bairro")
    private List<EstacaoRecarga> estacoesRecarga = new ArrayList<>();

    @OneToMany(mappedBy = "bairro")
    private List<Concessionaria> concessionarias = new ArrayList<>();

    // Getters e Setters

    public Long getBairroId() {
        return bairroId;
    }

    public void setBairroId(Long bairroId) {
        this.bairroId = bairroId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<EstacaoRecarga> getEstacoesRecarga() {
        return estacoesRecarga;
    }

    public void setEstacoesRecarga(List<EstacaoRecarga> estacoesRecarga) {
        this.estacoesRecarga = estacoesRecarga;
    }

    public List<Concessionaria> getConcessionarias() {
        return concessionarias;
    }

    public void setConcessionarias(List<Concessionaria> concessionarias) {
        this.concessionarias = concessionarias;
    }
}
