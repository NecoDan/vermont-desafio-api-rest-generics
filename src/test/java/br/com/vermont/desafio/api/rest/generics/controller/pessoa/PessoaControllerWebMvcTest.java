package br.com.vermont.desafio.api.rest.generics.controller.pessoa;

import br.com.vermont.desafio.api.rest.generics.pessoa.controller.PessoaController;
import br.com.vermont.desafio.api.rest.generics.pessoa.model.Pessoa;
import br.com.vermont.desafio.api.rest.generics.pessoa.model.TipoSexo;
import br.com.vermont.desafio.api.rest.generics.pessoa.repository.IPessoaRepository;
import br.com.vermont.desafio.api.rest.generics.pessoa.service.PessoaService;
import br.com.vermont.desafio.api.rest.generics.util.GeraCpfUtil;
import br.com.vermont.desafio.api.rest.generics.util.RandomicoUtil;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(PessoaController.class)
public class PessoaControllerWebMvcTest {

    private static final String BASE_URL = "/pessoas";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PessoaService pessoaService;

    @MockBean
    private IPessoaRepository pessoaRepository;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:dd"));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        this.objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @Test
    public void deveRetornarStatus201EProducerJSONAoCriarPessoaMethodPOSTSecondary() throws Exception {
        log.info("\n#TEST: deveRetornarStatus201EProducerJSONAoCriarPessoaMethodPOSTSecondary: ");

        // -- 01_Cenário
        Pessoa pessoa = constroiPessoaValida(null);

        // -- 02_Ação
        given(pessoaService.salvar(pessoa)).willReturn(pessoa);

        ResultActions responseResultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post(BASE_URL)
                        .content(getJsonValuePessoaFromPessoaObj(pessoa))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // -- 03_Verificação_Validação
        verify(pessoaService).salvar(any(Pessoa.class));

        MvcResult mvcResult = responseResultActions.andReturn();
        String conteudoJSONResult = mvcResult.getResponse().getContentAsString();
        Pessoa pessoaResult = getPessoaDeserializeFromConteudoJSON(conteudoJSONResult);

        assertTrue(Objects.equals(pessoa.getCpf(), pessoaResult.getCpf()) && Objects.equals(pessoa.getNome(), pessoaResult.getNome()));

        String statusResponse = String.valueOf(responseResultActions.andReturn().getResponse().getStatus());
        log.info("#TEST_RESULT_STATUS: ".concat((statusResponse.isEmpty()) ? " " : HttpStatus.valueOf(Integer.parseInt(statusResponse)).toString()));
        toStringEnd(responseResultActions, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarStatus201EProducerJSONAoCriarPessoaMethodPOST() throws Exception {
        log.info("\n#TEST: deveRetornarStatus201EProducerJSONAoCriarPessoaMethodPOST: ");

        // -- 01_Cenário
        Pessoa pessoa = constroiPessoaValida(null);

        // -- 02_Ação
        given(pessoaService.salvar(pessoa)).willReturn(pessoa);
        ResultActions responseResultActions = this.mockMvc.perform(post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .content(getJsonValuePessoaFromPessoaObj(pessoa))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        );

        // -- 03_Verificação_Validação
        responseResultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(pessoa.getId().toString()))
                .andExpect(jsonPath("$.nome").isNotEmpty())
                .andExpect(jsonPath("$.nome").value(pessoa.getNome()))
                .andExpect(jsonPath("$.cpf").isNotEmpty())
                .andExpect(jsonPath("$.cpf").value(pessoa.getCpf()))
                .andExpect(jsonPath("$.dataNascimento").isNotEmpty());

        verify(pessoaService).salvar(any(Pessoa.class));

        String statusResponse = String.valueOf(responseResultActions.andReturn().getResponse().getStatus());
        log.info("#TEST_RESULT_STATUS: ".concat((statusResponse.isEmpty()) ? " " : HttpStatus.valueOf(Integer.parseInt(statusResponse)).toString()));
        toStringEnd(responseResultActions, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarPeloMenosUnicaPessoa() throws Exception {
        log.info("\n#TEST: deveRetornarPeloMenosUnicaPessoa: ");

        // -- 01_Cenário
        UUID idPessoa = UUID.randomUUID();
        Pessoa pessoa = constroiPessoaValida(idPessoa);

        // -- 02_Ação
        given(pessoaService.recuperarPorId(idPessoa)).willReturn(Optional.of(pessoa));
        String uri = BASE_URL.concat("/").concat(idPessoa.toString());
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status()
                .isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(pessoa.getId().toString()))
                .andExpect(jsonPath("$.nome").isNotEmpty())
                .andExpect(jsonPath("$.nome").value(pessoa.getNome()))
                .andExpect(jsonPath("$.cpf").isNotEmpty())
                .andExpect(jsonPath("$.cpf").value(pessoa.getCpf()))
                .andExpect(jsonPath("$.dataNascimento").isNotEmpty());

        assertNotNull(response.andReturn().getResponse().getContentAsString());
        toStringEnd(response, MediaType.APPLICATION_JSON);
    }

    // -- VOLTAR O TESTE DEPOIS!!!!!!!!!!!!
//    @Test
    public void deveRetornarProducerJSONContendoUmaListaProdutosPorIntervaloDatasMethodGET() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaProdutosPorIntervaloDatasMethodGET: ");

        // -- 01_Cenário
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = LocalDate.now();

        List<Pessoa> pessoaList = Arrays.asList(constroiPessoaValida(null), constroiPessoaValida(null), constroiPessoaValida(null), constroiPessoaValida(null),
                constroiPessoaValida(null), constroiPessoaValida(null), constroiPessoaValida(null), constroiPessoaValida(null));

        // -- 02_Ação
        //given(produtoService.recuperarTodosPorPeriodo(dataInicio, dataFim)).willReturn(pessoaList);
        String uri = BASE_URL.concat("/buscarPorPeriodo?dataInicio=" + StringUtil.formatLocalDate(dataInicio) + "&" + "dataFim=" + StringUtil.formatLocalDate(dataInicio));
        ResultActions responseResultActions = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        responseResultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty());
        assertNotNull(responseResultActions.andReturn().getResponse().getContentAsString());

        toStringEnd(responseResultActions, MediaType.APPLICATION_JSON);
    }

    private void exibirLogResultPraVerificarRetorno(ResultActions resultActions) throws UnsupportedEncodingException {
        String resultStr = resultActions.andReturn().getResponse().getContentAsString();
        log.info("#TEST_RESULT: ".concat(resultStr));
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

    private Pessoa getPessoaDeserializeFromConteudoJSON(String json) throws JsonProcessingException {
        return this.objectMapper.readValue(json, Pessoa.class);
    }

    private String getJsonValuePessoaFromPessoaObj(Pessoa pessoa) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(pessoa);
    }

    private void toStringEnd(ResultActions response, MediaType mediaType) throws Exception {
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

    private ResultActions getResponseEntityEndPointsMethodGET(String url, MediaType mediaType) throws Exception {
        return this.mockMvc.perform(get(url).accept(mediaType));
    }
}
