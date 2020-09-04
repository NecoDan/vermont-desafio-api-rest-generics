package br.com.vermont.desafio.api.rest.generics.util.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class ResourceNotFoundDetails {
    private final String titulo;
    private final int status;
    private final String detalhes;
    private final Long timestamp;
    private final String mensagem;
}
