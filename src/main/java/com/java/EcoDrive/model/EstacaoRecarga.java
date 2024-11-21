package com.java.EcoDrive.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

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

    @Column(name = "latitude", nullable = false, precision = 10)
    private Double latitude;

    @Column(name = "longitude", nullable = false, precision = 10)
    private Double longitude;

    @Column(name = "tipo_carregador", nullable = false, length = 50)
    private String tipoCarregador;

    @Column(name = "preco_por_kwh", nullable = false, precision = 10)
    private Double precoPorKwh;

    // Relacionamentos
    @OneToOne(mappedBy = "estacaoRecarga")
    private EstacaoSustentavel estacaoSustentavel;

    @OneToMany(mappedBy = "estacaoRecarga")
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "estacaoRecarga")
    private List<HistoricoCarregamento> historicos = new ArrayList<>();

    @OneToMany(mappedBy = "estacaoRecarga")
    private List<StatusEstacaoRecarga> statusEstacoes = new ArrayList<>();

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

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<HistoricoCarregamento> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(List<HistoricoCarregamento> historicos) {
        this.historicos = historicos;
    }

    public List<StatusEstacaoRecarga> getStatusEstacoes() {
        return statusEstacoes;
    }

    public void setStatusEstacoes(List<StatusEstacaoRecarga> statusEstacoes) {
        this.statusEstacoes = statusEstacoes;
    }
}
