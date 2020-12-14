package br.com.vermont.desafio.api.rest.generics.enderecos.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Api(value = "SourceFont")
public class SourceController {

    private static final String URL_REPOSITORIO_GIT_HUB = "https://github.com/NecoDan/vermont-desafio-api-rest-generics";

    @ApiOperation(value = "Acessar o repósitório no GitHub pelo link externo")
    @GetMapping("/sources")
    public void getSouceInGitHub(HttpServletResponse httpServletResponse) {
        log.info("Requisição efetuada projeto GITHUB: " + URL_REPOSITORIO_GIT_HUB);
        httpServletResponse.setHeader("Location", URL_REPOSITORIO_GIT_HUB);
        httpServletResponse.setStatus(302);
    }
}
