package br.com.vermont.desafio.api.rest.generics.model.dominio;

import br.com.vermont.desafio.api.rest.generics.model.enums.TipoOperacaoLogEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Builder
@Data
@ToString
@Entity
@Table(name = "sp03_tipo_operacao_log", schema = "vermont_services")
@Inheritance(strategy = InheritanceType.JOINED)
public class TipoOperacaoLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Tolerate
    public TipoOperacaoLog() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "descricao")
    private String descricao;

    @Transient
    private TipoOperacaoLogEnum tipoOperacaoLogEnum;

    @JsonIgnore
    public TipoOperacaoLog definirTipoOperacaoLogEnum(TipoOperacaoLogEnum tipoOperacaoLogEnum) {
        if (Objects.isNull(tipoOperacaoLogEnum))
            throw new IllegalArgumentException("Código do Tipo de Operação Log inválido.");

        this.id = tipoOperacaoLogEnum.getCodigo();
        this.tipoOperacaoLogEnum = tipoOperacaoLogEnum;
        return this;
    }

    @JsonIgnore
    public boolean isIdValido() {
        return Objects.nonNull(this.getId());
    }

    @JsonIgnore
    public boolean isCreate() {
        return (isIdValido() && isTipoOperacaoLogEnumValido() && this.tipoOperacaoLogEnum.isCreate());
    }

    @JsonIgnore
    public boolean isUpdate() {
        return (isIdValido() && isTipoOperacaoLogEnumValido() && this.tipoOperacaoLogEnum.isUpdate());
    }

    @JsonIgnore
    public boolean isDelete() {
        return (isIdValido() && isTipoOperacaoLogEnumValido() && this.tipoOperacaoLogEnum.isDelete());
    }

    public boolean isTipoOperacaoLogEnumValido() {
        return (Objects.nonNull(this.tipoOperacaoLogEnum));
    }

    @JsonIgnore
    public boolean isEquals(TipoOperacaoLog otherTipoOperacaoLog) {
        return (Objects.nonNull(otherTipoOperacaoLog) && this.getId().equals(otherTipoOperacaoLog.getId()));
    }
}
