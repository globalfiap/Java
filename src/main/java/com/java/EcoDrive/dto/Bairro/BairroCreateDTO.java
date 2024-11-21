package com.java.EcoDrive.dto.Bairro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BairroCreateDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
