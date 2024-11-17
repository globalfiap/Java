package dto.Veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VeiculoCreateDTO {

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;

    @NotBlank(message = "A marca é obrigatória")
    @Size(max = 50, message = "A marca deve ter no máximo 50 caracteres")
    private String marca;

    @NotBlank(message = "O modelo é obrigatório")
    @Size(max = 50, message = "O modelo deve ter no máximo 50 caracteres")
    private String modelo;

    @NotNull(message = "O ano é obrigatório")
    private Integer ano;

    @NotNull(message = "O campo isEletrico é obrigatório")
    private Boolean isEletrico;

    // Getters e Setters

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

    public Boolean getIsEletrico() {
        return isEletrico;
    }

    public void setIsEletrico(Boolean isEletrico) {
        this.isEletrico = isEletrico;
    }
}
