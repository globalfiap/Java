package com.java.EcoDrive.dto.Veiculo;

public class VeiculoDTO {

    private Long veiculoId;
    private Long usuarioId;
    private String marca;
    private String modelo;
    private Integer ano;
    private Integer isEletrico;

    // Getters e Setters

    public Long getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
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
}
