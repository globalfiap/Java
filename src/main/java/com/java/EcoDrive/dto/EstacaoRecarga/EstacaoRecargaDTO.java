package com.java.EcoDrive.dto.EstacaoRecarga;

public class EstacaoRecargaDTO {

    private Long estacaoId;
    private String nome;
    private Long bairroId;
    private Double latitude;
    private Double longitude;
    private String tipoCarregador;
    private Double precoPorKwh;

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

    public Long getBairroId() {
        return bairroId;
    }

    public void setBairroId(Long bairroId) {
        this.bairroId = bairroId;
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
}
