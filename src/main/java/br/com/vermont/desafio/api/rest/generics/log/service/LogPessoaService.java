package br.com.vermont.desafio.api.rest.generics.log.service;

import br.com.vermont.desafio.api.rest.generics.log.model.LogPessoa;
import br.com.vermont.desafio.api.rest.generics.pessoa.model.Pessoa;
import br.com.vermont.desafio.api.rest.generics.log.model.TipoOperacaoLog;
import br.com.vermont.desafio.api.rest.generics.log.model.TipoOperacaoLogEnum;
import br.com.vermont.desafio.api.rest.generics.log.repository.ILogPessoaRepository;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogPessoaService implements ILogPessoaService {

    private final ILogPessoaRepository logPessoaRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(value = Transactional.TxType.NEVER)
    public void salvarLogAuditoriaTransacaoPessoa(Pessoa pessoa, TipoOperacaoLogEnum tipoOperacaoLog) throws ServiceException {
        this.logPessoaRepository.saveAndFlush(montarLogPessoaObjFromParams(pessoa, tipoOperacaoLog, null));
    }

    @Override
    public void salvarLogAuditoriaTransacaoPessoaAoDeletar(String conteudoJson, TipoOperacaoLogEnum tipoOperacaoLog) throws ServiceException {
        LogPessoa logPessoa = montarLogPessoaObjFromParams(null, tipoOperacaoLog, conteudoJson);
        this.logPessoaRepository.save(logPessoa);
    }

    @Override
    public String gerarConteudoJsonFromPessoa(Pessoa pessoa) throws JsonProcessingException {
        return (Objects.isNull(pessoa)) ? null : this.objectMapper.writeValueAsString(pessoa);
    }

    @Override
    @Transactional(value = Transactional.TxType.NEVER)
    public void atualizarLogsPessoaFromList(List<LogPessoa> logPessoaList) {
        if (Objects.isNull(logPessoaList) || logPessoaList.isEmpty())
            return;

        logPessoaList.stream().filter(Objects::nonNull).filter(logPessoa -> Objects.nonNull(logPessoa.getPessoa())).forEach(logPessoa -> logPessoa.setPessoa(null));
        this.logPessoaRepository.saveAll(logPessoaList);
    }

    @Override
    public List<LogPessoa> recuperarLogsPessoaPorPessoa(Pessoa pessoa) {
        return this.logPessoaRepository.findAllByPessoa(pessoa);
    }

    private LogPessoa montarLogPessoaObjFromParams(Pessoa pessoa, TipoOperacaoLogEnum tipoOperacaoLog, String conteudoJson) throws ServiceException {
        try {
            LogPessoa logPessoa = LogPessoa
                    .builder()
                    .pessoa(pessoa)
                    .dataUltimaAtualizacao(getUltimaDataAtualizacaoToLogPessoa(pessoa, tipoOperacaoLog))
                    .tipoOperacaoLog(TipoOperacaoLog
                            .builder()
                            .build()
                            .definirTipoOperacaoLogEnum(tipoOperacaoLog))
                    .conteudo((Objects.isNull(conteudoJson) || conteudoJson.isEmpty()) ? getConteudoJsonFromPessoa(pessoa) : conteudoJson)
                    .build();

            logPessoa.geraId();
            logPessoa.gerarDataCorrente();
            return logPessoa;
        } catch (JsonProcessingException e) {
            throw new ServiceException("Erro ao efetuar a conversão do conteudo JSON LOG: " + e.getLocalizedMessage());
        }
    }

    private LocalDateTime getUltimaDataAtualizacaoToLogPessoa(Pessoa pessoa, TipoOperacaoLogEnum tipoOperacaoLog) {
        return (Objects.isNull(pessoa) || tipoOperacaoLog.isDelete()) ? LocalDateTime.now() : pessoa.getDataCadastro();
    }

    private String getConteudoJsonFromPessoa(Pessoa pessoa) throws JsonProcessingException {
        return (isParamValidosParaRetornarConteudoJsonFromPessoa(pessoa)) ? gerarConteudoJsonFromPessoa(pessoa) : getConteudoJsonFromPessoaNotNull(pessoa);
    }

    private boolean isParamValidosParaRetornarConteudoJsonFromPessoa(Pessoa pessoa) {
        return (Objects.nonNull(pessoa) && Objects.isNull(pessoa.getConteudoJson()));
    }

    private String getConteudoJsonFromPessoaNotNull(Pessoa pessoa) {
        return (Objects.nonNull(pessoa)) ? pessoa.getConteudoJson() : "";
    }
}
