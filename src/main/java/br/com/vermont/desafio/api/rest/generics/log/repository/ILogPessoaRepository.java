package br.com.vermont.desafio.api.rest.generics.log.repository;

import br.com.vermont.desafio.api.rest.generics.log.model.LogPessoa;
import br.com.vermont.desafio.api.rest.generics.pessoa.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ILogPessoaRepository extends JpaRepository<LogPessoa, UUID> {
    List<LogPessoa> findAllByPessoa(@Param("pesssoa") Pessoa pessoa);
}
