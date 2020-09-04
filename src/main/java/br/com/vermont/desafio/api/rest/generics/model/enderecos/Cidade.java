package br.com.vermont.desafio.api.rest.generics.model.enderecos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cidade {
    private String area_km2;
    private String codigo_ibge;
}
