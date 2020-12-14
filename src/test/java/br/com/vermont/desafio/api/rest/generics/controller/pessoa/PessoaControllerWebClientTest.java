package br.com.vermont.desafio.api.rest.generics.controller.pessoa;


import br.com.vermont.desafio.api.rest.generics.config.AdviceControllerConfig;
import br.com.vermont.desafio.api.rest.generics.pessoa.controller.PessoaController;
import br.com.vermont.desafio.api.rest.generics.pessoa.model.Pessoa;
import br.com.vermont.desafio.api.rest.generics.pessoa.model.TipoSexo;
import br.com.vermont.desafio.api.rest.generics.pessoa.service.PessoaService;
import br.com.vermont.desafio.api.rest.generics.util.FormatterUtil;
import br.com.vermont.desafio.api.rest.generics.util.GeraCpfUtil;
import br.com.vermont.desafio.api.rest.generics.util.RandomicoUtil;
import br.com.vermont.desafio.api.rest.generics.util.exceptions.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@Slf4j
public class PessoaControllerWebClientTest {

    private static final String BASE_URI = "/pessoas";

    private PessoaService pessoaService;
    private WebTestClient webTestClient;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.pessoaService = Mockito.mock(PessoaService.class);
        PessoaController pessoaController = new PessoaController(this.pessoaService);

        webTestClient = WebTestClient
                .bindToController(pessoaController)
                .controllerAdvice(AdviceControllerConfig.class)
                .build();

        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(ofPattern("yyyy-MM-dd HH:mm:ss")));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(ofPattern("yyyy-MM-dd")));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(ofPattern("yyyy-MM-dd")));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ofPattern("yyyy-MM-dd HH:mm:ss")));

        this.objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @Test
    public void deveRetornarPageUmOuMaisPessoasMethodGET() {
        log.info("{} ", "\n#TEST: deveRetornarPageUmOuMaisPessoasMethodGET: ");

        // -- 01_Cenário
        AtomicReference<String> resultJson = new AtomicReference<>("");
        List<Pessoa> pessoaList = Arrays.asList(constroiPessoaValida(null), constroiPessoaValida(null), constroiPessoaValida(null));
        Pessoa pessoaPosicao1Lista = pessoaList.get(0);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Pessoa> pagePessoa = new PageImpl<>(pessoaList, pageable, pessoaList.size());

        // -- 02_Ação
        String uri = BASE_URI.concat("/");
        when(pessoaService.recuperarTodos(pageable)).thenReturn(pagePessoa);
        assertTrue(resultJson.get().isEmpty());

        log.info("{} ", "\n#TEST: deveRetornarPageUmOuMaisPessoasMethodGET: ");
        toStringEnd(resultJson, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarPeloMenosUnicaPessoaMethodGET() {
        log.info("{} ", "\n#TEST: deveRetornarPeloMenosUnicaPessoaMethodGET: ");

        // -- 01_Cenário
        AtomicReference<String> resultJson = new AtomicReference<>("");
        UUID id = UUID.randomUUID();
        Pessoa pessoa = constroiPessoaValida(id);

        // -- 02_Ação
        String uri = BASE_URI.concat("/").concat(id.toString());
        when(pessoaService.recuperarPorId(id)).thenReturn(Optional.of(pessoa));

        // -- 03_Verificação_Validação
        webTestClient
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Pessoa.class)
                .consumeWith(pessoaResourceResult -> {
                    try {
                        resultJson.set(this.objectMapper.writeValueAsString(Objects.requireNonNull(pessoaResourceResult.getResponseBody())));
                        assertThat(Objects.requireNonNull(pessoaResourceResult.getResponseBody()).getId()).isEqualTo(id);
                    } catch (JsonProcessingException e) {
                        log.info(e.getLocalizedMessage());
                    }
                });

        log.info("{} ", "\n#TEST: deveRetornarPeloMenosUnicaPessoaMethodGET: ");
        toStringEnd(resultJson, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarPeloMenosUnicaPessoaAoSalvarMethodPOST() throws ServiceException, JsonProcessingException {
        log.info("{} ", "\n#TEST: deveRetornarPeloMenosUnicaPessoaAoSalvarMethodPOST: ");

        // -- 01_Cenário
        AtomicReference<String> resultJson = new AtomicReference<>("");
        Pessoa pessoa = constroiPessoaValida(null);

        // -- 02_Ação
        when(pessoaService.salvar(pessoa)).thenReturn(pessoa);

        // -- 03_Verificação_Validação
        resultJson.set(this.objectMapper.writeValueAsString(pessoa));
        this.webTestClient
                .post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(pessoa), Pessoa.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.cpf").isNotEmpty()
                .jsonPath("$.cpf").isEqualTo(pessoa.getCpf())
                .jsonPath("$.dataNascimento").isNotEmpty()
                .jsonPath("$.dataNascimento").exists()
                .jsonPath("$.dataNascimento").isEqualTo(FormatterUtil.toStringLocalDateFormatada(pessoa.getDataNascimento()));

        log.info("{} ", "\n#TEST: deveRetornarPeloMenosUnicaPessoaAoSalvarMethodPOST: ");
        toStringEnd(resultJson, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarPeloMenosUnicaPessoaAoAtualizarMethodPUT() throws ServiceException, JsonProcessingException {
        log.info("{} ", "\n#TEST: deveRetornarPeloMenosUnicaPessoaAoAtualizarMethodPUT: ");

        // -- 01_Cenário
        AtomicReference<String> resultJson = new AtomicReference<>("");
        UUID idPessoa = UUID.randomUUID();
        Pessoa pessoa = constroiPessoaValida(idPessoa);

        // -- 02_Ação
        when(pessoaService.atualizar(idPessoa, pessoa)).thenReturn(pessoa);

        // -- 03_Verificação_Validação
        resultJson.set(this.objectMapper.writeValueAsString(pessoa));
        this.webTestClient
                .put()
                .uri(BASE_URI + "/" + idPessoa.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(pessoa), Pessoa.class)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.cpf").isNotEmpty()
                .jsonPath("$.cpf").isEqualTo(pessoa.getCpf())
                .jsonPath("$.dataNascimento").isNotEmpty()
                .jsonPath("$.dataNascimento").exists()
                .jsonPath("$.dataNascimento").isEqualTo(FormatterUtil.toStringLocalDateFormatada(pessoa.getDataNascimento()));

        log.info("{} ", "\n#TEST: deveRetornarPeloMenosUnicaPessoaAoAtualizarMethodPUT: ");
        toStringEnd(resultJson, MediaType.APPLICATION_JSON);
    }

    private Pessoa constroiPessoaValida(UUID id) {
        Pessoa pessoa = Pessoa.builder()
                .nome(String.valueOf(RandomicoUtil.gerarValorRandomico()))
                .cpf(GeraCpfUtil.cpf(false))
                .dataNascimento(LocalDate.now())
                .tipoSexo(TipoSexo.M)
                .build();

        pessoa.setId(Objects.isNull(id) ? UUID.randomUUID() : id);
        pessoa.ativado();
        pessoa.gerarDataCorrente();
        return pessoa;
    }

    private void toStringEnd(AtomicReference<String> resultJson, MediaType mediaType) {
        if (Objects.isNull(resultJson) || Objects.isNull(mediaType)) {
            log.info("{} ", "#TEST_RESULT: ".concat("Error ao gerar saida. Não existem dados..."));
            log.info("{} ", "-------------------------------------------------------------");
            return;
        }

        String result = resultJson.get();
        String out = "";

        if (mediaType == MediaType.APPLICATION_JSON)
            out = FormatterUtil.formatConteudoJSONFrom(result);

        log.info("{} ", "#TEST_RESULT: ".concat(out));
        log.info("{} ", "-------------------------------------------------------------");
    }
}
