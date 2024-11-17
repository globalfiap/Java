package dto.StatusEstacaoRecarga;

import jakarta.validation.constraints.*;

public class StatusEstacaoRecargaCreateDTO {

    @NotNull(message = "O ID da estação é obrigatório")
    private Long estacaoId;

    @NotBlank(message = "O status é obrigatório")
    @Pattern(regexp = "Ativa|Defeituosa|Em Manutenção", message = "O status deve ser 'Ativa', 'Defeituosa' ou 'Em Manutenção'")
    private String status;

    // Getters e Setters

    public Long getEstacaoId() {
        return estacaoId;
    }

    public void setEstacaoId(Long estacaoId) {
        this.estacaoId = estacaoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
