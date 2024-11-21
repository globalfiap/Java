package com.java.EcoDrive.dto.Concessionaria;

import jakarta.validation.constraints.*;

public class ConcessionariaCreateDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(message = "O ID do bairro é obrigatório")
    private Long bairroId;

    @NotBlank(message = "A marca é obrigatória")
    @Size(max = 50, message = "A marca deve ter no máximo 50 caracteres")
    private String marca;

    @NotNull(message = "O campo temEstacaoRecarga é obrigatório")
    private Boolean temEstacaoRecarga;

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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public @NotNull(message = "O campo temEstacaoRecarga é obrigatório") Boolean getTemEstacaoRecarga() {
        return temEstacaoRecarga;
    }

    public void setTemEstacaoRecarga(Boolean temEstacaoRecarga) {
        this.temEstacaoRecarga = temEstacaoRecarga;
    }
}
