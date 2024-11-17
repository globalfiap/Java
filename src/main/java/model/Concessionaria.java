package model;

import jakarta.persistence.*;

@Entity
@Table(name = "concessionaria")
public class Concessionaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concessionaria_id")
    private Long concessionariaId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "bairro_id", nullable = false)
    private Bairro bairro;

    @Column(name = "marca", nullable = false, length = 50)
    private String marca;

    @Column(name = "tem_estacao_recarga", nullable = false)
    private Integer temEstacaoRecarga;

    // Getters e Setters

    public Long getConcessionariaId() {
        return concessionariaId;
    }

    public void setConcessionariaId(Long concessionariaId) {
        this.concessionariaId = concessionariaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getTemEstacaoRecarga() {
        return temEstacaoRecarga;
    }

    public void setTemEstacaoRecarga(Integer temEstacaoRecarga) {
        this.temEstacaoRecarga = temEstacaoRecarga;
    }
}
