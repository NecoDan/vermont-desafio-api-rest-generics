package br.com.vermont.desafio.api.rest.generics.perfil.controller;

import br.com.vermont.desafio.api.rest.generics.perfil.exceptions.PerfilNotFoundException;
import br.com.vermont.desafio.api.rest.generics.perfil.model.Perfil;
import br.com.vermont.desafio.api.rest.generics.perfil.model.TokenPerfil;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ResourceStatusNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class PerfilController {

    private static final String OAM_TOKEN_HEADER = "authenticatedState";
    private static final String MENSAGEM_TOKEN_INVALIDO = "Nenhum {TOKEN} definido como parâmetro e/ou inválido.";

    @GetMapping("/findByToken")
    public ResponseEntity<Perfil> getOne(@RequestHeader(value = OAM_TOKEN_HEADER) String authenticatedState) {
        validarTokenChaveValorAuthenticatedState(authenticatedState);
        Map<String, String> mapHeader = montarMapHeaderFromParams(authenticatedState);
        return getResponseEntityByOnePerfil(mapHeader, authenticatedState);
    }

    @GetMapping("/findByHeader")
    public ResponseEntity<Perfil> getOneByHeaderComplete(HttpServletRequest request) {
        String requestKeyAuthenticatedState = request.getHeader(OAM_TOKEN_HEADER);
        validarTokenChaveValorAuthenticatedState(requestKeyAuthenticatedState);
        Map<String, String> mapHeader = montarMapHeaderFromParams(requestKeyAuthenticatedState);
        return getResponseEntityByOnePerfil(mapHeader, requestKeyAuthenticatedState);
    }

    @GetMapping("/findByTokenPerfil")
    public ResponseEntity<Perfil> getOneFromToken(@RequestParam("token") String token) {
        validarTokenChaveValorAuthenticatedState(token);
        return new ResponseEntity<>(obterPerfil(TokenPerfil.builder().token(token).build()).orElseThrow(() -> new PerfilNotFoundException(token)), HttpStatus.OK);
    }

    private ResponseEntity<Perfil> getResponseEntityByOnePerfil(Map<String, String> mapHeader, String tokenAuthenticatedStateValue) {
        Optional<Perfil> optionalPerfil = obterPerfil(montarHeaderTokenPerfilPorMapHeader(mapHeader));
        return new ResponseEntity<>(optionalPerfil.orElseThrow(() -> new PerfilNotFoundException(tokenAuthenticatedStateValue)), HttpStatus.OK);
    }

    private void validarTokenChaveValorAuthenticatedState(String tokenAuthenticatedStateValue) {
        if (Objects.isNull(tokenAuthenticatedStateValue) || tokenAuthenticatedStateValue.isEmpty())
            throw new ResourceStatusNotFoundException(MENSAGEM_TOKEN_INVALIDO);
    }

    private Optional<Perfil> obterPerfil(TokenPerfil tokenPerfil) {
        List<Perfil> perfis = Perfil.getListAll();
        return perfis.stream().filter(Objects::nonNull).filter(p -> p.getToken().equals(tokenPerfil.getToken())).findAny();
    }

    private Map<String, String> montarMapHeaderFromParams(String tokenAuthenticatedStateValue) {
        Map<String, String> mapHeader = new HashMap<>();
        mapHeader.put(OAM_TOKEN_HEADER, tokenAuthenticatedStateValue);
        return mapHeader;
    }

    private TokenPerfil montarHeaderTokenPerfilPorMapHeader(Map<String, String> mapHeader) {
        return TokenPerfil
                .builder()
                .token(mapHeader.get(OAM_TOKEN_HEADER))
                .build();
    }
}
