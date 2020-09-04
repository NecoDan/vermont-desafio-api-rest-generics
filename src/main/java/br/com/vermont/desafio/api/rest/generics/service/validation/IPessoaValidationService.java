package br.com.vermont.desafio.api.rest.generics.service.validation;

import br.com.vermont.desafio.api.rest.generics.model.dominio.Pessoa;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ServiceException;

public interface IPessoaValidationService {

    void validarSomentePessoaIsValida(Pessoa pessoa) throws ServiceException;

    void validarPessoa(Pessoa pessoa) throws ServiceException;
}
