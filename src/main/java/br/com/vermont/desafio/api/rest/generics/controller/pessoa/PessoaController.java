package br.com.vermont.desafio.api.rest.generics.controller.pessoa;

import br.com.vermont.desafio.api.rest.generics.model.dominio.Pessoa;
import br.com.vermont.desafio.api.rest.generics.service.negocio.IPessoaService;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ResourceStatusNotFoundException;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Api(value = "Pessoa")
public class PessoaController {

    private final IPessoaService pessoaService;
    private static final String MSG_VALIDACAO_NOT_FOUND = "Server Response: Nenhuma pessoa(s) encontrada(s).";

    @ApiOperation(value = "Retorna todos as Pessoas existentes com paginação")
    @GetMapping("/pessoas")
    public ResponseEntity<Page<Pessoa>> getAllPessoas(@PageableDefault(page = 0, size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Pessoa> pessoaPage = this.pessoaService.recuperarTodos(pageable);
        return (pessoaPage.isEmpty()) ? new ResponseEntity(MSG_VALIDACAO_NOT_FOUND, HttpStatus.NOT_FOUND) : new ResponseEntity<Page<Pessoa>>(pessoaPage, HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna um única pessoa existente, caso exista, a partir de seu id {UIID} registrado.")
    @GetMapping("/pessoas/{id}")
    public ResponseEntity<Pessoa> getOnePessoa(@PathVariable(value = "id") String id) {
        return new ResponseEntity<>(pessoaService.recuperarPorId(UUID.fromString(id)).orElseThrow(() -> new ResourceStatusNotFoundException(MSG_VALIDACAO_NOT_FOUND)), HttpStatus.OK);
    }

    @ApiOperation(value = "Responsável por persistir uma única instância de Pessoa, a partir de um consumer {Pessoa} passado como parâmetro no corpo da requisição...")
    @PostMapping("/pessoas")
    public ResponseEntity<Pessoa> savePessoa(@RequestBody @Valid Pessoa pessoa) {
        try {
            return new ResponseEntity<>(this.pessoaService.salvar(pessoa), HttpStatus.CREATED);
        } catch (ServiceException e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Responsável por excluir um Pessoa, a partir de um @PathVariable contendo o id {UIID} do registro.")
    @DeleteMapping("/pessoas/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePessoa(@PathVariable(value = "id") String id) {
        try {
            return (pessoaService.excluirPor(UUID.fromString(id))) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(MSG_VALIDACAO_NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (ServiceException e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Responsável por atualizar uma Pessoa, a partir de um @PathVariable contendo o id {UIID} do registro e um consumer {Pessoa} como corpo da requisição.")
    @PutMapping("/pessoas/{id}")
    public ResponseEntity<Pessoa> updatePessoa(@PathVariable(value = "id") String id,
                                               @RequestBody @Valid Pessoa pessoa) {
        try {
            Optional<Pessoa> pessoaOptional = Optional.of(pessoaService.atualizar(UUID.fromString(id), pessoa));
            return pessoaOptional.map(p -> new ResponseEntity<>(p, HttpStatus.ACCEPTED)).orElseGet(() -> new ResponseEntity(MSG_VALIDACAO_NOT_FOUND, HttpStatus.NOT_FOUND));
        } catch (ServiceException e) {
            log.error(e.getLocalizedMessage());
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
