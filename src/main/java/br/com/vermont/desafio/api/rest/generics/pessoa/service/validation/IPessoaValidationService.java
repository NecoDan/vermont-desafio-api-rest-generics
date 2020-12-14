package br.com.vermont.desafio.api.rest.generics.pessoa.service.validation;

import br.com.vermont.desafio.api.rest.generics.pessoa.model.Pessoa;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ServiceException;

public interface IPessoaValidationService {

    void validarSomentePessoaIsValida(Pessoa pessoa) throws ServiceException;

    void validarPessoa(Pessoa pessoa) throws ServiceException;
}
