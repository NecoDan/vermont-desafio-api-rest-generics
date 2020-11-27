package br.com.vermont.desafio.api.rest.generics.exceptions;

import java.util.UUID;

public class PerfilNotFoundException extends RuntimeException {

    private static final String MENSAGEM = "Perfil com {TOKEN} = %s, não encontrado e/ou localizado.";

    public PerfilNotFoundException(UUID token) {
        super(String.format(MENSAGEM, token.toString()));
    }

    public PerfilNotFoundException(String token) {
        super(String.format(MENSAGEM, token));
    }
}
