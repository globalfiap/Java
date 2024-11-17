package model;

import jakarta.persistence.*;
import java.util.Set;

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
    private Set<EstacaoRecarga> estacoesRecarga;

    @OneToMany(mappedBy = "bairro")
    private Set<Concessionaria> concessionarias;

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

    public Set<EstacaoRecarga> getEstacoesRecarga() {
        return estacoesRecarga;
    }

    public void setEstacoesRecarga(Set<EstacaoRecarga> estacoesRecarga) {
        this.estacoesRecarga = estacoesRecarga;
    }

    public Set<Concessionaria> getConcessionarias() {
        return concessionarias;
    }

    public void setConcessionarias(Set<Concessionaria> concessionarias) {
        this.concessionarias = concessionarias;
    }
}
