package br.com.vermont.desafio.api.rest.generics.util.domain;

public class CustomErrorType {

    private final String erroMensagem;

    public CustomErrorType(String erroMensagem) {
        this.erroMensagem = erroMensagem;
    }

    public String getErroMensagem() {
        return erroMensagem;
    }
}
