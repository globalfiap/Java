package model;

import jakarta.persistence.*;

@Entity
@Table(name = "veiculo", uniqueConstraints = @UniqueConstraint(columnNames = "marca"))
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "veiculo_id")
    private Long veiculoId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "marca", nullable = false, length = 50)
    private String marca;

    @Column(name = "modelo", nullable = false, length = 50)
    private String modelo;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "is_eletrico", nullable = false)
    private Integer isEletrico;

    // Relacionamentos
    @OneToMany(mappedBy = "veiculo")
    private Set<HistoricoCarregamento> historicos;

    // Getters e Setters

    public Long getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getIsEletrico() {
        return isEletrico;
    }

    public void setIsEletrico(Integer isEletrico) {
        this.isEletrico = isEletrico;
    }

    public Set<HistoricoCarregamento> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(Set<HistoricoCarregamento> historicos) {
        this.historicos = historicos;
    }
}
