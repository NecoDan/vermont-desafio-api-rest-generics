package br.com.vermont.desafio.api.rest.generics.controller.pessoa;

import br.com.vermont.desafio.api.rest.generics.exceptions.PessoaNotFoundException;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ResourceStatusNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class AdvicePessoaController extends ResponseEntityExceptionHandler {

    private static final String CHAVE_MAP_TIMESTAMP = "timestamp";

    @ExceptionHandler(PessoaNotFoundException.class)
    public ResponseEntity<Object> handleCityNotFoundException(PessoaNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(CHAVE_MAP_TIMESTAMP, LocalDateTime.now());
        body.put("message", "Pessoa não encontrada.");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceStatusNotFoundException.class)
    public ResponseEntity<Object> handleNodataFoundException(ResourceStatusNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(CHAVE_MAP_TIMESTAMP, LocalDateTime.now());
        body.put("message", "Nenhuma pessoa(s) encontrada.");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(CHAVE_MAP_TIMESTAMP, LocalDate.now());
        body.put("status", status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
