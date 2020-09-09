package br.com.vermont.desafio.api.rest.generics.service.negocio;

import br.com.vermont.desafio.api.rest.generics.model.dominio.LogPessoa;
import br.com.vermont.desafio.api.rest.generics.model.dominio.Pessoa;
import br.com.vermont.desafio.api.rest.generics.model.enums.TipoOperacaoLogEnum;
import br.com.vermont.desafio.api.rest.generics.repository.negocio.IPessoaRepository;
import br.com.vermont.desafio.api.rest.generics.service.log.ILogPessoaService;
import br.com.vermont.desafio.api.rest.generics.service.validation.IPessoaValidationService;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PessoaService implements IPessoaService {

    private final IPessoaRepository pessoaRepository;
    private final ILogPessoaService logPessoaService;
    private final IPessoaValidationService pessoaValidationService;

    @Override
    public Optional<Pessoa> recuperarPorId(UUID id) {
        return this.pessoaRepository.findById(id);
    }

    @Override
    public Page<Pessoa> recuperarTodos(Pageable pageable) {
        return this.pessoaRepository.findAll(pageable);
    }

    @Override
    @Transactional(value = Transactional.TxType.NEVER)
    public Pessoa salvar(Pessoa pessoa) throws ServiceException {
        Optional<Pessoa> pessoaExistsComCpfParam = this.pessoaRepository.findByCpfEquals(pessoa.getCpf());

        if (pessoaExistsComCpfParam.isPresent())
            throw new ServiceException("Já existe uma {Pessoa} de Nome: " + pessoa.getNome() + " registrada com o CPF: " + pessoa.getCpf());

        this.pessoaValidationService.validarPessoa(pessoa);

        pessoa.geraId();
        pessoa.gerarDataCorrente();
        pessoa.ativado();
        Pessoa pessoaInsert = this.pessoaRepository.saveAndFlush(pessoa);

        this.logPessoaService.salvarLogAuditoriaTransacaoPessoa(pessoaInsert, TipoOperacaoLogEnum.CREATE);
        return pessoaInsert;
    }

    @Override
    @Transactional(value = Transactional.TxType.NEVER)
    public Pessoa atualizar(UUID idPessoa, Pessoa pessoa) throws ServiceException {
        if (Objects.isNull(idPessoa))
            throw new ServiceException("{ID} referente a Pessoa encontra-se inválida e/ou inexistente {NULL}.");

        Pessoa pessoaAtualizar = recuperarPorId(idPessoa).map(p -> getMountPessoaAtualizar(p, pessoa)).orElse(null);

        if (Objects.isNull(pessoaAtualizar))
            throw new ServiceException("Nenhuma {PESSOA} encontrada ou não localizada com o {ID} = {%s}. Pessoa inválida e/ou inexistente {NULL}.".replace("%s", idPessoa.toString()));

        this.pessoaRepository.save(Objects.requireNonNull(pessoaAtualizar));
        this.logPessoaService.salvarLogAuditoriaTransacaoPessoa(pessoaAtualizar, TipoOperacaoLogEnum.UPDATE);
        return pessoaAtualizar;
    }

    private Pessoa getMountPessoaAtualizar(Pessoa p, Pessoa pessoaParam) {
        p.setNome((Objects.isNull(pessoaParam.getNome()) || pessoaParam.getNome().isEmpty()) ? p.getNome() : pessoaParam.getNome());
        p.setCpf((Objects.isNull(pessoaParam.getCpf()) || pessoaParam.getCpf().isEmpty()) ? p.getCpf() : pessoaParam.getCpf());
        p.setDataNascimento(Objects.isNull(pessoaParam.getDataNascimento()) ? p.getDataNascimento() : pessoaParam.getDataNascimento());
        p.setTipoSexo(Objects.isNull(pessoaParam.getTipoSexo()) ? p.getTipoSexo() : pessoaParam.getTipoSexo());
        p.setEmail((Objects.isNull(pessoaParam.getEmail()) || pessoaParam.getEmail().isEmpty()) ? p.getEmail() : pessoaParam.getEmail());
        p.setNaturalidade((Objects.isNull(pessoaParam.getNaturalidade()) || pessoaParam.getNaturalidade().isEmpty()) ? p.getNaturalidade() : pessoaParam.getNaturalidade());
        p.setNacionalidade((Objects.isNull(pessoaParam.getNacionalidade()) || pessoaParam.getNacionalidade().isEmpty()) ? p.getNacionalidade() : pessoaParam.getNacionalidade());
        p.setDataCadastro(LocalDateTime.now());
        p.ativado();
        return p;
    }

    @Override
    @Transactional(value = Transactional.TxType.NEVER)
    public boolean excluir(Pessoa pessoa) throws ServiceException {
        return (Objects.nonNull(pessoa)) && excluirPor(pessoa.getId());
    }

    private Optional<Pessoa> getPessoaAEfetuarExclusao(UUID id) throws ServiceException {
        if (Objects.isNull(id))
            throw new ServiceException("{ID} referente a Pessoa, encontra-se inválido e/ou inexistente {NULL}.");

        Optional<Pessoa> optionalPessoa = recuperarPorId(id);
        if (!optionalPessoa.isPresent())
            throw new ServiceException("Nenhuma Pessoa encontrada com a referência do {ID} - {" + id.toString() + "} passado.");

        return optionalPessoa;
    }

    @Override
    @Transactional(value = Transactional.TxType.NEVER)
    public boolean excluirPor(UUID id) throws ServiceException {
        Optional<Pessoa> optionalPessoa = getPessoaAEfetuarExclusao(id);

        try {
            Pessoa pessoa = optionalPessoa.get();
            String conteudoJson = this.logPessoaService.gerarConteudoJsonFromPessoa(pessoa);

            List<LogPessoa> logPessoaList = this.logPessoaService.recuperarLogsPessoaPorPessoa(pessoa);
            this.logPessoaService.atualizarLogsPessoaFromList(logPessoaList);

            this.pessoaRepository.deleteById(pessoa.getId());
            this.logPessoaService.salvarLogAuditoriaTransacaoPessoaAoDeletar(conteudoJson, TipoOperacaoLogEnum.DELETE);
            return true;
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
        }
        return false;
    }
}
