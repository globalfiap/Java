package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_carregamento")
public class HistoricoCarregamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historico_id")
    private Long historicoId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "estacao_id", nullable = false)
    private EstacaoRecarga estacaoRecarga;

    @Column(name = "data_carregamento", nullable = false)
    private LocalDateTime dataCarregamento;

    @Column(name = "kwh_consumidos", nullable = false, precision = 10, scale = 2)
    private Double kwhConsumidos;

    // Relacionamentos
    @OneToOne(mappedBy = "historicoCarregamento")
    private GastoCarregamento gastoCarregamento;

    // Getters e Setters

    public Long getHistoricoId() {
        return historicoId;
    }

    public void setHistoricoId(Long historicoId) {
        this.historicoId = historicoId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public EstacaoRecarga getEstacaoRecarga() {
        return estacaoRecarga;
    }

    public void setEstacaoRecarga(EstacaoRecarga estacaoRecarga) {
        this.estacaoRecarga = estacaoRecarga;
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

    public GastoCarregamento getGastoCarregamento() {
        return gastoCarregamento;
    }

    public void setGastoCarregamento(GastoCarregamento gastoCarregamento) {
        this.gastoCarregamento = gastoCarregamento;
    }
}
