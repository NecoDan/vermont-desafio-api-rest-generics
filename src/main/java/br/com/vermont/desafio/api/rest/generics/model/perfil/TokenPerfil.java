package br.com.vermont.desafio.api.rest.generics.model.perfil;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPerfil {
    private String token;
}
