package dto.EstacaoSustentavel;

public class EstacaoSustentavelDTO {

    private Long estacaoId;
    private Long fonteId;
    private Double reducaoCarbono;

    // Getters e Setters

    public Long getEstacaoId() {
        return estacaoId;
    }

    public void setEstacaoId(Long estacaoId) {
        this.estacaoId = estacaoId;
    }

    public Long getFonteId() {
        return fonteId;
    }

    public void setFonteId(Long fonteId) {
        this.fonteId = fonteId;
    }

    public Double getReducaoCarbono() {
        return reducaoCarbono;
    }

    public void setReducaoCarbono(Double reducaoCarbono) {
        this.reducaoCarbono = reducaoCarbono;
    }
}
