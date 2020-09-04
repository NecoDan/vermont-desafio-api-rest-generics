package br.com.vermont.desafio.api.rest.generics.service.negocio;

import br.com.vermont.desafio.api.rest.generics.model.dominio.Pessoa;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

public interface IPessoaService {

    Optional<Pessoa> recuperarPorId(UUID id);

    Page<Pessoa> recuperarTodos(Pageable pageable);

    @Transactional(value = Transactional.TxType.NEVER)
    Pessoa salvar(Pessoa pessoa) throws ServiceException;

    @Transactional(value = Transactional.TxType.NEVER)
    Pessoa atualizar(UUID produtoId, Pessoa pessoa) throws ServiceException;

    @Transactional(value = Transactional.TxType.NEVER)
    boolean excluir(Pessoa pessoa) throws ServiceException;

    @Transactional(value = Transactional.TxType.NEVER)
    boolean excluirPor(UUID id) throws ServiceException;
}
