package br.com.vermont.desafio.api.rest.generics.repository.log;

import br.com.vermont.desafio.api.rest.generics.model.dominio.LogPessoa;
import br.com.vermont.desafio.api.rest.generics.model.dominio.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ILogPessoaRepository extends JpaRepository<LogPessoa, UUID> {
    List<LogPessoa> findAllByPessoa(@Param("pesssoa") Pessoa pessoa);
}
