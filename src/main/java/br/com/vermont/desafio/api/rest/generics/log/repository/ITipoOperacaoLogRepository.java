package br.com.vermont.desafio.api.rest.generics.log.repository;

import br.com.vermont.desafio.api.rest.generics.log.model.TipoOperacaoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoOperacaoLogRepository extends JpaRepository<TipoOperacaoLog, Integer> {
}
