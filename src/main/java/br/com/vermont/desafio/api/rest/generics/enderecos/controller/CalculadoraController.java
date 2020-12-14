package br.com.vermont.desafio.api.rest.generics.enderecos.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/calculadora")
@RequiredArgsConstructor
@Api(value = "Calculadora")
public class CalculadoraController {

    private static final String CHAVE_SOMA = "chave-soma";

    @GetMapping("/soma/{firstNumber}/{secondNumber}")
    public ResponseEntity<BigDecimal> sumTwoNumbers(@PathVariable("firstNumber") BigDecimal firstNumber,
                                                    @PathVariable("secondNumber") BigDecimal secondNumber,
                                                    HttpServletRequest httpServletRequest) {
        String chaveRequest = httpServletRequest.getHeader(CHAVE_SOMA);

        if (Objects.isNull(chaveRequest) || chaveRequest.isEmpty())
            chaveRequest = UUID.randomUUID().toString();

        log.info("{}", "Calculadora Controller started... {sumTwoNumbers}");
        log.info("{}", "First number: " + firstNumber.doubleValue());
        log.info("{}", "Second number: " + secondNumber.doubleValue());

        BigDecimal soma = firstNumber.add(secondNumber);
        HttpHeaders httpResponseHeaders = new HttpHeaders();
        httpResponseHeaders.set(CHAVE_SOMA, chaveRequest);

        return new ResponseEntity<>(soma, httpResponseHeaders, HttpStatus.OK);
    }
}
