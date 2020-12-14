package br.com.vermont.desafio.api.rest.generics.pessoa.exceptions;

import java.util.UUID;

public class PessoaNotFoundException extends RuntimeException {
    public PessoaNotFoundException(UUID id) {
        super("Pessoa com {ID} = {" + id + "} não encontrada e/ou localizada.");
    }
}
