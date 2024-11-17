package model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "estacao_recarga")
public class EstacaoRecarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estacao_id")
    private Long estacaoId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "bairro_id", nullable = false)
    private Bairro bairro;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 6)
    private Double latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 6)
    private Double longitude;

    @Column(name = "tipo_carregador", nullable = false, length = 50)
    private String tipoCarregador;

    @Column(name = "preco_por_kwh", nullable = false, precision = 10, scale = 2)
    private Double precoPorKwh;

    // Relacionamentos
    @OneToOne(mappedBy = "estacaoRecarga")
    private EstacaoSustentavel estacaoSustentavel;

    @OneToMany(mappedBy = "estacaoRecarga")
    private Set<Reserva> reservas;

    @OneToMany(mappedBy = "estacaoRecarga")
    private Set<HistoricoCarregamento> historicos;

    @OneToMany(mappedBy = "estacaoRecarga")
    private Set<StatusEstacaoRecarga> statusEstacoes;

    // Getters e Setters

    public Long getEstacaoId() {
        return estacaoId;
    }

    public void setEstacaoId(Long estacaoId) {
        this.estacaoId = estacaoId;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTipoCarregador() {
        return tipoCarregador;
    }

    public void setTipoCarregador(String tipoCarregador) {
        this.tipoCarregador = tipoCarregador;
    }

    public Double getPrecoPorKwh() {
        return precoPorKwh;
    }

    public void setPrecoPorKwh(Double precoPorKwh) {
        this.precoPorKwh = precoPorKwh;
    }

    public EstacaoSustentavel getEstacaoSustentavel() {
        return estacaoSustentavel;
    }

    public void setEstacaoSustentavel(EstacaoSustentavel estacaoSustentavel) {
        this.estacaoSustentavel = estacaoSustentavel;
    }

    public Set<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(Set<Reserva> reservas) {
        this.reservas = reservas;
    }

    public Set<HistoricoCarregamento> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(Set<HistoricoCarregamento> historicos) {
        this.historicos = historicos;
    }

    public Set<StatusEstacaoRecarga> getStatusEstacoes() {
        return statusEstacoes;
    }

    public void setStatusEstacoes(Set<StatusEstacaoRecarga> statusEstacoes) {
        this.statusEstacoes = statusEstacoes;
    }
}
