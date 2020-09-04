package br.com.vermont.desafio.api.rest.generics.repository.log;

import br.com.vermont.desafio.api.rest.generics.model.dominio.TipoOperacaoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoOperacaoLogRepository extends JpaRepository<TipoOperacaoLog, Integer> {
}
