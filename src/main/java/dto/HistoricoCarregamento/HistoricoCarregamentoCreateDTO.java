package dto.HistoricoCarregamento;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class HistoricoCarregamentoCreateDTO {

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "O ID do veículo é obrigatório")
    private Long veiculoId;

    @NotNull(message = "O ID da estação é obrigatório")
    private Long estacaoId;

    @NotNull(message = "A data de carregamento é obrigatória")
    private LocalDateTime dataCarregamento;

    @NotNull(message = "Os kWh consumidos são obrigatórios")
    private Double kwhConsumidos;

    // Getters e Setters

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }

    public Long getEstacaoId() {
        return estacaoId;
    }

    public void setEstacaoId(Long estacaoId) {
        this.estacaoId = estacaoId;
    }

    public LocalDateTime getDataCarregamento() {
        return dataCarregamento;
    }

    public void setDataCarregamento(LocalDateTime dataCarregamento) {
        this.dataCarregamento = dataCarregamento;
    }

    public Double getKwhConsumidos() {
        return kwhConsumidos;
    }

    public void setKwhConsumidos(Double kwhConsumidos) {
        this.kwhConsumidos = kwhConsumidos;
    }
}
