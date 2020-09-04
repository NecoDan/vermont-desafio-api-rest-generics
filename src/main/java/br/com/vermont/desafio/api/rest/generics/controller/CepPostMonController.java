package br.com.vermont.desafio.api.rest.generics.controller;

import br.com.vermont.desafio.api.rest.generics.model.enderecos.Endereco;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Api(value = "Endereços_Api_Externa")
public class CepPostMonController {

    public static final String BASE_URL = "https://api.postmon.com.br/v1/cep/";
    private static final String MSG_VALIDACAO_NOT_FOUND = "Server Response: Nenhuma endereço(s) ou cep(s) encontrado(s).";

    @ApiOperation(value = "Retorna um único Endereço existente, caso exista, a partir de seu CEP registrado.")
    @GetMapping("/enderecos/{cep}")
    public ResponseEntity<Endereco> getOneEndereco(@PathVariable(value = "cep") String cep) {
        try {
            String uri = BASE_URL + cep;
            RestTemplate restTemplate = new RestTemplate();

            Endereco endereco = restTemplate.getForObject(uri, Endereco.class);
            return (Objects.isNull(endereco)) ? new ResponseEntity(MSG_VALIDACAO_NOT_FOUND, HttpStatus.NOT_FOUND) : new ResponseEntity<>(endereco, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
