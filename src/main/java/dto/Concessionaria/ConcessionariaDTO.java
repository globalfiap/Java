package dto.Concessionaria;

public class ConcessionariaDTO {

    private Long concessionariaId;
    private String nome;
    private Long bairroId;
    private String marca;
    private Boolean temEstacaoRecarga;

    // Getters e Setters

    public Long getConcessionariaId() {
        return concessionariaId;
    }

    public void setConcessionariaId(Long concessionariaId) {
        this.concessionariaId = concessionariaId;
    }

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

    public Boolean getTemEstacaoRecarga() {
        return temEstacaoRecarga;
    }

    public void setTemEstacaoRecarga(Boolean temEstacaoRecarga) {
        this.temEstacaoRecarga = temEstacaoRecarga;
    }
}
