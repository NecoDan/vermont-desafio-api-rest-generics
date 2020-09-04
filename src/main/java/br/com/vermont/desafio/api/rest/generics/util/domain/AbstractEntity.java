package br.com.vermont.desafio.api.rest.generics.util.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@SuperBuilder
@Data
@EqualsAndHashCode(of = "id")
public class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Tolerate
    public AbstractEntity() {
    }

    @Id
    @Column(name = "id")
    @Setter(value = AccessLevel.PUBLIC)
    private UUID id;

    @Column(name = "dt_cadastro")
    @Setter(value = AccessLevel.PUBLIC)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", locale = "pt-BR", timezone = "UTC")
    private LocalDateTime dataCadastro;

    public void gerarDataCorrente() {
        if (Objects.isNull(this.getDataCadastro()))
            this.setDataCadastro(LocalDateTime.now());
    }

    public void geraId() {
        if (Objects.isNull(this.id))
            this.setId(UUID.randomUUID());
    }
}
