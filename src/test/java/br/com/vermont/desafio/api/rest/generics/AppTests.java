package br.com.vermont.desafio.api.rest.generics;

import br.com.vermont.desafio.api.rest.generics.log.model.LogPessoa;
import br.com.vermont.desafio.api.rest.generics.pessoa.model.Pessoa;
import br.com.vermont.desafio.api.rest.generics.log.model.TipoOperacaoLog;
import br.com.vermont.desafio.api.rest.generics.pessoa.model.TipoSexo;
import br.com.vermont.desafio.api.rest.generics.log.repository.ILogPessoaRepository;
import br.com.vermont.desafio.api.rest.generics.log.repository.ITipoOperacaoLogRepository;
import br.com.vermont.desafio.api.rest.generics.pessoa.repository.IPessoaRepository;
import br.com.vermont.desafio.api.rest.generics.util.GeraCpfUtil;
import br.com.vermont.desafio.api.rest.generics.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = App.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AppTests {

    private static final String BASE_URL = "/pessoas";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ITipoOperacaoLogRepository tipoOperacaoLogRepository;

    @Autowired
    private ILogPessoaRepository logPessoaRepository;

    @Autowired
    private IPessoaRepository pessoaRepository;

    private ObjectMapper objectMapper;
    private Pessoa pessoaContextoAppTest;

    @Before
    public void setUp() throws Exception {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:dd"));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        this.objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        this.pessoaContextoAppTest = constroiPessoaValida(null, "Menino Jesus Dos Santos");
        deverCriarTiposOperacaoLogPadrao();
    }

    @Test
    public void contextLoads() throws Exception {
        log.info("{}", "TEST: Startando execuções de testes de integração...");
        deveRetornarStatus201EProducerJSONAoCriarPessoaMethodPOSTWebMvcTestH2();
        deveRetornarPeloMenosUnicaPessoaMethodGETWebMvcTestH2();
        deveRetornarStatus202EProducerJSONAoAtualizarPessoaMethodPUTWebMvcTestH2();
        deveRetornarUmaListaPessoasComProducerJSONMethodGETWebMvcTestH2();
        deveRetornarStatus200EProducerJSONAoDeletarPessoaMethodDELETEWebMvcTestH2();
        log.info("{}", "TEST: Finalizando execuções de testes de integração... ");
    }

    private void deverCriarTiposOperacaoLogPadrao() {
        List<TipoOperacaoLog> tipoOperacaoLogList = Arrays.asList(
                TipoOperacaoLog.builder().id(1).descricao("CREATE").build(),
                TipoOperacaoLog.builder().id(2).descricao("UPDATE").build(),
                TipoOperacaoLog.builder().id(3).descricao("DELETE").build()
        );

        log.info("{}", "TODOS OS TIPOS DE OPERAÇÃO LOGS DEFAULT DEVEM SER CRIADOS AO START NA APLICAÇÃO ");
        log.info("{}", tipoOperacaoLogList.toString());
        tipoOperacaoLogRepository.saveAll(tipoOperacaoLogList);
    }

    public void deveRetornarStatus201EProducerJSONAoCriarPessoaMethodPOSTWebMvcTestH2() throws Exception {
        log.info("\n#TEST: deveRetornarStatus201EProducerJSONAoCriarPessoaMethodPOSTWebMvcTestH2: ");

        // -- 01_Cenário
        Pessoa pessoaSaveMethodPost = this.pessoaContextoAppTest;

        // -- 02_Ação
        ResultActions responseResultActions = this.mockMvc.perform(post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .content(getJsonValuePessoaFromPessoaObj(pessoaSaveMethodPost))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        );

        // -- 03_Verificação_Validação
        responseResultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(pessoaSaveMethodPost.getId().toString()))
                .andExpect(jsonPath("$.nome").isNotEmpty())
                .andExpect(jsonPath("$.nome").value(pessoaSaveMethodPost.getNome()))
                .andExpect(jsonPath("$.cpf").isNotEmpty())
                .andExpect(jsonPath("$.cpf").value(pessoaSaveMethodPost.getCpf()))
                .andExpect(jsonPath("$.dataNascimento").isNotEmpty());

        assertNotNull(responseResultActions.andReturn().getResponse().getContentAsString());

        String statusResponse = String.valueOf(responseResultActions.andReturn().getResponse().getStatus());
        log.info("#METHOD_POST - TEST_RESULT_STATUS: ".concat((statusResponse.isEmpty()) ? " " : HttpStatus.valueOf(Integer.parseInt(statusResponse)).toString()));

        toStringEndPorResultActions(responseResultActions, MediaType.APPLICATION_JSON);
    }

    public void deveRetornarPeloMenosUnicaPessoaMethodGETWebMvcTestH2() throws Exception {
        log.info("\n#TEST: deveRetornarPeloMenosUnicaPessoaMethodGETWebMvcTestH2: ");

        // -- 01_Cenário
        UUID idPessoa = this.pessoaContextoAppTest.getId();
        Pessoa pessoaParam = this.pessoaContextoAppTest;

        // -- 02_Ação
        String uri = BASE_URL.concat("/").concat(idPessoa.toString());
        ResultActions responseResultActions = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        responseResultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(pessoaParam.getId().toString()))
                .andExpect(jsonPath("$.nome").isNotEmpty())
                .andExpect(jsonPath("$.nome").value(pessoaParam.getNome()))
                .andExpect(jsonPath("$.cpf").isNotEmpty())
                .andExpect(jsonPath("$.cpf").value(pessoaParam.getCpf()))
                .andExpect(jsonPath("$.dataNascimento").isNotEmpty());

        assertNotNull(responseResultActions.andReturn().getResponse().getContentAsString());

        String statusResponse = String.valueOf(responseResultActions.andReturn().getResponse().getStatus());
        log.info("#METHOD_GET - TEST_RESULT_STATUS: ".concat((statusResponse.isEmpty()) ? " " : HttpStatus.valueOf(Integer.parseInt(statusResponse)).toString()));

        toStringEndPorResultActions(responseResultActions, MediaType.APPLICATION_JSON);
    }

    public void deveRetornarUmaListaPessoasComProducerJSONMethodGETWebMvcTestH2() throws Exception {
        log.info("\n#TEST: deveRetornarUmaListaPessoasComProducerJSONMethodGETWebMvcTestH2: ");

        // -- 01_Cenário
        int totalElementosResult = this.pessoaRepository.findAll().size();
        boolean isVazio = totalElementosResult <= 0;

        // -- 02_Ação
        String uri = BASE_URL.concat("/");
        ResultActions responseResultActions = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        responseResultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalPages").isNotEmpty())
                .andExpect(jsonPath("$.totalElements").isNotEmpty())
                .andExpect(jsonPath("$.numberOfElements").isNotEmpty())
                .andExpect(jsonPath("$.totalPages").value(String.valueOf(totalElementosResult)))
                .andExpect(jsonPath("$.totalElements").value(String.valueOf(totalElementosResult)))
                .andExpect(jsonPath("$.numberOfElements").value(String.valueOf(totalElementosResult)))
                .andExpect(jsonPath("$.empty").value(isVazio));

        assertNotNull(responseResultActions.andReturn().getResponse().getContentAsString());

        String statusResponse = String.valueOf(responseResultActions.andReturn().getResponse().getStatus());
        log.info("#METHOD_GET - TEST_RESULT_STATUS: ".concat((statusResponse.isEmpty()) ? " " : HttpStatus.valueOf(Integer.parseInt(statusResponse)).toString()));

        toStringEndPorResultActions(responseResultActions, MediaType.APPLICATION_JSON);
    }

    public void deveRetornarStatus202EProducerJSONAoAtualizarPessoaMethodPUTWebMvcTestH2() throws Exception {
        log.info("\n#TEST: deveRetornarStatus202EProducerJSONAoAtualizarPessoaMethodPUTWebMvcTestH2: ");

        // -- 01_Cenário
        UUID idPessoa = this.pessoaContextoAppTest.getId();

        Pessoa pessoaUpdateMethodPut = Pessoa.builder().build();
        pessoaUpdateMethodPut.setNome(this.pessoaContextoAppTest.getNome());
        pessoaUpdateMethodPut.setCpf(this.pessoaContextoAppTest.getCpf());
        pessoaUpdateMethodPut.setDataNascimento(this.pessoaContextoAppTest.getDataNascimento());

        pessoaUpdateMethodPut.setEmail("teste@teste.mail.com");
        pessoaUpdateMethodPut.setNaturalidade("Brasileiro");
        pessoaUpdateMethodPut.setNacionalidade("Brasileiro");

        // -- 02_Ação
        String uri = BASE_URL.concat("/").concat(idPessoa.toString());

        ResultActions responseResultActions = this.mockMvc.perform(put(uri)
                .accept(MediaType.APPLICATION_JSON)
                .content(getJsonValuePessoaFromPessoaObj(pessoaUpdateMethodPut))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        );

        // -- 03_Verificação_Validação
        responseResultActions
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").isNotEmpty())
                .andExpect(jsonPath("$.cpf").isNotEmpty())
                .andExpect(jsonPath("$.dataNascimento").isNotEmpty())
                .andExpect(jsonPath("$.email").value(pessoaUpdateMethodPut.getEmail()))
                .andExpect(jsonPath("$.naturalidade").value(pessoaUpdateMethodPut.getNaturalidade()))
                .andExpect(jsonPath("$.nacionalidade").value(pessoaUpdateMethodPut.getNacionalidade()));

        assertNotNull(responseResultActions.andReturn().getResponse().getContentAsString());

        String statusResponse = String.valueOf(responseResultActions.andReturn().getResponse().getStatus());
        log.info("#METHOD_PUT - TEST_RESULT_STATUS: ".concat((statusResponse.isEmpty()) ? " " : HttpStatus.valueOf(Integer.parseInt(statusResponse)).toString()));

        toStringEndPorResultActions(responseResultActions, MediaType.APPLICATION_JSON);
    }

    public void deveRetornarStatus200EProducerJSONAoDeletarPessoaMethodDELETEWebMvcTestH2() throws Exception {
        log.info("\n#TEST: deveRetornarStatus200EProducerJSONAoDeletarPessoaMethodDELETEWebMvcTestH2: ");

        // -- 01_Cenário
        UUID idPessoa = this.pessoaContextoAppTest.getId();

        // -- 02_Ação
        String uri = BASE_URL.concat("/").concat(idPessoa.toString());

        ResultActions responseResultActions = this.mockMvc
                .perform(delete(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        // -- 03_Verificação_Validação
        responseResultActions.andExpect(status().isOk());

        String statusResponse = String.valueOf(responseResultActions.andReturn().getResponse().getStatus());
        log.info("#METHOD_DELETE - TEST_RESULT_STATUS: ".concat((statusResponse.isEmpty()) ? " " : HttpStatus.valueOf(Integer.parseInt(statusResponse)).toString()));

        List<LogPessoa> logPessoaList = this.logPessoaRepository.findAll();
        Optional<LogPessoa> logPessoaDelete = logPessoaList.stream().filter(Objects::nonNull).filter(LogPessoa::isTipoOperacaoLogDelete).findFirst();

        logPessoaDelete.ifPresent(logPessoa ->
                assertTrue((Objects.nonNull(logPessoa.getConteudo())
                        && !logPessoa.getConteudo().isEmpty()
                        && logPessoa.getConteudo().contains(idPessoa.toString()))));

        logPessoaDelete.ifPresent(logPessoa -> log.info("#CONTEUDO_LOG_PESSOA_DELETE: ".concat(logPessoa.getConteudo())));
    }

    private ResultActions getResponseEntityEndPointsMethodGET(String url, MediaType mediaType) throws Exception {
        return this.mockMvc.perform(get(url).accept(mediaType));
    }

    private MvcResult getResponseMvcResultEntityEndPointsMethodGET(String url, MediaType mediaType) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders.get(url).accept(mediaType)).andReturn();
    }

    private String getJsonValuePessoaFromPessoaObj(Pessoa pessoa) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(pessoa);
    }

    private void toStringEndPorMvcResult(MvcResult mvcResult, MediaType mediaType) throws Exception {
        if (Objects.isNull(mvcResult) || Objects.isNull(mediaType)) {
            log.info("#TEST_RESULT: ".concat("Error ao gerar saida. Não existem dados..."));
            log.info("-------------------------------------------------------------");
            return;
        }

        String result = mvcResult.getResponse().getContentAsString();
        String out = StringUtil.formatConteudoJSONFrom(result);

        log.info("#TEST_RESULT: ".concat(out));
        log.info("-------------------------------------------------------------");
    }

    private void toStringEndPorResultActions(ResultActions response, MediaType mediaType) throws Exception {
        if (Objects.isNull(response) || Objects.isNull(mediaType)) {
            log.info("#TEST_RESULT: ".concat("Error ao gerar saida. Não existem dados..."));
            log.info("-------------------------------------------------------------");
            return;
        }

        String result = response.andReturn().getResponse().getContentAsString();
        String out = "";

        if (mediaType == MediaType.APPLICATION_JSON)
            out = StringUtil.formatConteudoJSONFrom(result);

        if (mediaType == MediaType.APPLICATION_XML)
            out = StringUtil.formatConteudoXMLFrom(result);

        log.info("#TEST_RESULT: ".concat(out));
        log.info("-------------------------------------------------------------");
    }

    private Pessoa constroiPessoaValida(UUID id, String nome) {
        Pessoa pessoa = Pessoa.builder()
                .nome(nome)
                .cpf(GeraCpfUtil.cpf(false))
                .dataNascimento(LocalDate.now())
                .tipoSexo(TipoSexo.M)
                .build();

        pessoa.setId(Objects.isNull(id) ? UUID.randomUUID() : id);
        pessoa.ativado();
        return pessoa;
    }
}

