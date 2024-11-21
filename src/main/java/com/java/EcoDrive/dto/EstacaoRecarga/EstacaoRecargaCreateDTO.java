package com.java.EcoDrive.dto.EstacaoRecarga;

import jakarta.validation.constraints.*;

public class EstacaoRecargaCreateDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(message = "O ID do bairro é obrigatório")
    private Long bairroId;

    @NotNull(message = "A latitude é obrigatória")
    private Double latitude;

    @NotNull(message = "A longitude é obrigatória")
    private Double longitude;

    @NotBlank(message = "O tipo de carregador é obrigatório")
    @Size(max = 50, message = "O tipo de carregador deve ter no máximo 50 caracteres")
    private String tipoCarregador;

    @NotNull(message = "O preço por kWh é obrigatório")
    private Double precoPorKwh;

    // Getters e Setters

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
