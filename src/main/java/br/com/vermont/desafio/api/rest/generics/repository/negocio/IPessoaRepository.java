package br.com.vermont.desafio.api.rest.generics.repository.negocio;

import br.com.vermont.desafio.api.rest.generics.model.dominio.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPessoaRepository extends JpaRepository<Pessoa, UUID> {

    Optional<Pessoa> findByCpfEquals(@Param("cpf") String cpf);

    Page<Pessoa> findAllByAtivo(@Param("ativo") boolean ativo, Pageable pageable);

    Page<Pessoa> findAllByNomeContainingIgnoreCase(@Param("nome") String descricao, Pageable pageable);

}
