package br.com.vermont.desafio.api.rest.generics.controller.perfil;

import br.com.vermont.desafio.api.rest.generics.exceptions.PerfilNotFoundException;
import br.com.vermont.desafio.api.rest.generics.exceptions.ResourceUnauthorizedException;
import br.com.vermont.desafio.api.rest.generics.util.FormatterUtil;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ResourceStatusNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class AdvicePerfilController extends ResponseEntityExceptionHandler {

    private static final String CHAVE_MAP_TIMESTAMP = "timestamp";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handlerInternalServerErrorFromExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PerfilNotFoundException.class)
    public ResponseEntity<Object> handlerPedidoNotFoundException(PerfilNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceStatusNotFoundException.class)
    public ResponseEntity<Object> handlerNoDataFoundException(ResourceStatusNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceUnauthorizedException.class)
    public ResponseEntity<Object> handlerNoUnauthorizedException(ResourceUnauthorizedException ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex.getLocalizedMessage(), HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }

    private Map<String, Object> getBody(String message, HttpStatus httpStatus) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put(CHAVE_MAP_TIMESTAMP, FormatterUtil.toStringLocalDateTimeFormatada(LocalDateTime.now()));
        body.put("message", message);
        body.put("status_code", httpStatus.value());
        body.put("status", httpStatus.toString());

        return body;
    }
}
