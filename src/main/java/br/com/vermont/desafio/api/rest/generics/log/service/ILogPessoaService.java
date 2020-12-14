package br.com.vermont.desafio.api.rest.generics.log.service;

import br.com.vermont.desafio.api.rest.generics.log.model.LogPessoa;
import br.com.vermont.desafio.api.rest.generics.pessoa.model.Pessoa;
import br.com.vermont.desafio.api.rest.generics.log.model.TipoOperacaoLogEnum;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.transaction.Transactional;
import java.util.List;

public interface ILogPessoaService {

    @Transactional(value = Transactional.TxType.NEVER)
    void salvarLogAuditoriaTransacaoPessoa(Pessoa pessoa, TipoOperacaoLogEnum tipoOperacaoLog) throws ServiceException;

    void salvarLogAuditoriaTransacaoPessoaAoDeletar(String conteudoJson, TipoOperacaoLogEnum tipoOperacaoLog) throws ServiceException;

    String gerarConteudoJsonFromPessoa(Pessoa pessoa) throws JsonProcessingException;

    @Transactional(value = Transactional.TxType.NEVER)
    void atualizarLogsPessoaFromList(List<LogPessoa> logPessoaList);

    List<LogPessoa> recuperarLogsPessoaPorPessoa(Pessoa pessoa);
}
