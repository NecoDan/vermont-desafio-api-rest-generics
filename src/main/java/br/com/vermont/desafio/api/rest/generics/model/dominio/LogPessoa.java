package br.com.vermont.desafio.api.rest.generics.model.dominio;

import br.com.vermont.desafio.api.rest.generics.model.enums.TipoOperacaoLogEnum;
import br.com.vermont.desafio.api.rest.generics.util.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sp02_log_pessoa", schema = "vermont_services")
@Inheritance(strategy = InheritanceType.JOINED)
public class LogPessoa extends AbstractEntity {

    @Tolerate
    public LogPessoa() {
        super();
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa")
    private Pessoa pessoa;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_tipo_operacao")
    private TipoOperacaoLog tipoOperacaoLog;

    @Column(name = "conteudo", length = 800)
    private String conteudo;

    @JsonIgnore
    @Column(name = "dt_ultima_atualizacao")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", locale = "pt-BR", timezone = "UTC")
    private LocalDateTime dataUltimaAtualizacao;

    @JsonIgnore
    public boolean isTipoOperacaoDelete() {
        return (Objects.nonNull(this.tipoOperacaoLog) && tipoOperacaoLog.isDelete());
    }

    public boolean isTipoOperacaoLogDelete() {
        return (Objects.nonNull(this.tipoOperacaoLog) && tipoOperacaoLog.getId().equals(TipoOperacaoLogEnum.DELETE.getCodigo()));
    }
}
