package br.com.vermont.desafio.api.rest.generics.perfil.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPerfil {
    private String token;
}
