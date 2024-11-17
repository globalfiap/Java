package dto.FonteEnergia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class FonteEnergiaCreateDTO {

    @NotBlank(message = "O tipo de energia é obrigatório")
    @Pattern(regexp = "Paineis Solares|Energia Comum", message = "O tipo de energia deve ser 'Paineis Solares' ou 'Energia Comum'")
    private String tipoEnergia;

    // Getters e Setters

    public String getTipoEnergia() {
        return tipoEnergia;
    }

    public void setTipoEnergia(String tipoEnergia) {
        this.tipoEnergia = tipoEnergia;
    }
}
