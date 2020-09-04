package br.com.vermont.desafio.api.rest.generics.service.validation;

import br.com.vermont.desafio.api.rest.generics.model.dominio.Pessoa;
import br.com.vermont.desafio.api.rest.generics.util.CpfUtil;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ServiceException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PessoaValidationService implements IPessoaValidationService {

    @Override
    public void validarSomentePessoaIsValida(Pessoa pessoa) throws ServiceException {
        if (Objects.isNull(pessoa))
            throw new ServiceException("Pessoa encontra-se inválida e/ou inexistente {NULL}. Nenhuma referência do objeto {PESSOA} localizada.");
    }

    @Override
    public void validarPessoa(Pessoa pessoa) throws ServiceException {
        validarSomentePessoaIsValida(pessoa);

        if (Objects.isNull(pessoa.getCpf()))
            throw new ServiceException("CPF referente à {Pessoa}, encontra-se inexistente {NULL}. Insira um CPF e tente novamente");

        if (!CpfUtil.isCPFValido(pessoa.getCpf()))
            throw new ServiceException("CPF referente à {Pessoa}, encontra-se inválido. Não é um CPF válido e aceitável. Insira um CPF e tente novamente");
    }
}
