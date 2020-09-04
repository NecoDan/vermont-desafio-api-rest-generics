package br.com.vermont.desafio.api.rest.generics.model.enderecos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {
    private String bairro;
    private String cidade;
    private String cep;
    private String estado;
    private Cidade cidade_info;
    private Estado estado_info;
}
