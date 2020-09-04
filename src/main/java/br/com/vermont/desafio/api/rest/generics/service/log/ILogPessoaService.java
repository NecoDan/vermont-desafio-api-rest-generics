package br.com.vermont.desafio.api.rest.generics.service.log;

import br.com.vermont.desafio.api.rest.generics.model.dominio.LogPessoa;
import br.com.vermont.desafio.api.rest.generics.model.dominio.Pessoa;
import br.com.vermont.desafio.api.rest.generics.model.enums.TipoOperacaoLogEnum;
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
