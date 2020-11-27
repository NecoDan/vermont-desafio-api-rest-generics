package br.com.vermont.desafio.api.rest.generics.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ResourceUnauthorizedException extends RuntimeException {
    public ResourceUnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED.toString().concat(message));
    }
}
