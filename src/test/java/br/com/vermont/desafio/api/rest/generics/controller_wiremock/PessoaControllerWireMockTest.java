package br.com.vermont.desafio.api.rest.generics.controller_wiremock;

import br.com.vermont.desafio.api.rest.generics.pessoa.model.Pessoa;
import br.com.vermont.desafio.api.rest.generics.util.GeraCpfUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@Slf4j
public class PessoaControllerWireMockTest {

    private static final String URL_BASE = "localhost";
    private static final String URL_COMPLEMENTO = "/pessoas";
    private static final int PORT = 8094;
    private static final String URL_COMPLETA_ACESSO_SERVER = "http://localhost:8094/pessoas";

    // instanciando e inicializar o web service
    @Rule
    public WireMockRule wireMockServerRuleJunit = new WireMockRule(PORT);

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        // definindo configurações e fragmentando o web service
        stubFor(get(urlPathMatching("/pessoas/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("\"testing-library\": \"Pessoa\"")));

        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:dd"));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        this.objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @After
    public void after() {
        if (Objects.nonNull(this.wireMockServerRuleJunit))
            this.wireMockServerRuleJunit.stop();
    }

    @Test
    public void deveInicializacaoServerInWireMockServerRuleJunit() {
        log.info("\n#TEST: deveInicializacaoServerInWireMockServerRuleJunit: ");

        // -- 01_Cenário && -- 02_Ação
        String mensagemInicializacaoWireMock = (Objects.isNull(this.wireMockServerRuleJunit))
                ? "Houve erro ao inicializar server WireMock...."
                : "Server WireMock inicializado com sucesso....";

        // -- 03_Verificação_Validação
        Assert.assertNotNull(this.wireMockServerRuleJunit);
        log.info("{}", mensagemInicializacaoWireMock);
    }

    @Test
    public void deveAceitarRequisicaoPorMeioJsonNoBodEmServerLocalHost() throws IOException {
        log.info("\n#TEST: deveAceitarRequisicaoPorMeioJsonNoBodEmServerLocalHost: ");

        // -- 01_Cenário
        // simulando um cliente se conectando ao server por meio HttpClients
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // -- 02_Ação
        // executando Request e obtendo o Response
        HttpGet request = new HttpGet(URL_COMPLETA_ACESSO_SERVER.concat("/").concat("pessoa"));
        HttpResponse httpResponse = httpClient.execute(request);

        // -- 03_Verificação_Validação
        // após obter o Response, convertendo em string a resposta
        String strResponse = convertHttpResponseToString(httpResponse);

        verify(getRequestedFor(urlEqualTo("/pessoas/pessoa")));
        assertEquals(HttpStatus.OK, HttpStatus.valueOf(httpResponse.getStatusLine().getStatusCode()));
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals("\"testing-library\": \"Pessoa\"", strResponse);

        log.info("{}", strResponse);
    }

    @Test
    public void deveTestarAoDefinirConfigDoStubApiRESTCorrespondenteAoHeader() throws IOException {
        log.info("\n#TEST: deveTestarAoDefinirConfigDoStubApiRESTCorrespondenteAoHeader: ");

        // -- 01_Cenário
        stubFor(get(urlPathEqualTo("/pessoas/pessoa"))
                .withHeader("Accept", matching("text/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SERVICE_UNAVAILABLE.value())
                        .withHeader("Content-Type", "text/html")
                        .withBody("!!! Service Unavailable !!!")));

        // -- 02_Ação
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(URL_COMPLETA_ACESSO_SERVER.concat("/").concat("pessoa"));
        request.addHeader("Accept", "text/html");

        HttpResponse httpResponse = httpClient.execute(request);

        // -- 03_Verificação_Validação
        String stringResponse = convertHttpResponseToString(httpResponse);

        verify(getRequestedFor(urlEqualTo("/pessoas/pessoa")));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.valueOf(httpResponse.getStatusLine().getStatusCode()));
        assertEquals("text/html", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals("!!! Service Unavailable !!!", stringResponse);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PessoaTemp {
        String nome;
    }

    @Test
    public void deveTestarAoDefinirConfigDoStubApiRESTCorrespondenteAoBody() throws IOException {
        log.info("\n#TEST: deveTestarAoDefinirConfigDoStubApiRESTCorrespondenteAoBody: ");

        // -- 01_Cenário
        UUID id = UUID.randomUUID();
        String cpf = GeraCpfUtil.cpf(false);

        Pessoa pessoaResult = Pessoa.builder()
                .id(id)
                .nome("Daniel")
                .cpf(cpf)
                .email("teste@gov.br")
                .nacionalidade("Brasil")
                .naturalidade("Sao Luis")
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .dataNascimento(LocalDate.now().minusYears(30))
                .build();

        Pessoa pessoaResponseParams = Pessoa.builder()
                .nome(pessoaResult.getNome())
                .cpf(pessoaResult.getCpf())
                .dataNascimento(pessoaResult.getDataNascimento())
                .tipoSexo(pessoaResult.getTipoSexo())
                .email(pessoaResult.getEmail())
                .naturalidade(pessoaResult.getNaturalidade())
                .nacionalidade(pessoaResult.getNacionalidade())
                .build();

        HttpStatus httpStatusEsperado = HttpStatus.ACCEPTED;
        String conteudoJsonBodyResponse = getJsonValuePessoaFromPessoaObj(pessoaResponseParams);
        String conteudoJsonBodyResult = getJsonValuePessoaFromPessoaObj(pessoaResult);

        wireMockServerRuleJunit.stubFor(post("/pessoas/")
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalToJson(conteudoJsonBodyResponse))
                .willReturn(aResponse()
                        .withStatus(httpStatusEsperado.value())
                )
                .willReturn(aResponse()
                        .withBody(conteudoJsonBodyResult)
                )
        );

        StringEntity entity = getStringEntityFrom(conteudoJsonBodyResponse);
        log.info("{}", getStringResultRequestBodyFromEntity(entity));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(URL_COMPLETA_ACESSO_SERVER + "/");
        request.addHeader("Content-Type", "application/json");
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        HttpStatus httpStatusResult = (Objects.equals(HttpStatus.valueOf(response.getStatusLine().getStatusCode()), HttpStatus.OK))
                ? HttpStatus.ACCEPTED
                : HttpStatus.INTERNAL_SERVER_ERROR;

        verify(postRequestedFor(urlEqualTo("/pessoas/"))
                .withHeader("Content-Type", equalTo("application/json")));
        assertEquals(httpStatusEsperado, httpStatusResult);
    }

    private String getStringResultRequestBodyFromEntity(StringEntity entity) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(entity.getContent(), writer, "UTF-8");
        return writer.toString();
    }

    private Pessoa getPessoaDeserializeFromConteudoJSON(String json) throws JsonProcessingException {
        return this.objectMapper.readValue(json, Pessoa.class);
    }

    private String getJsonValuePessoaFromPessoaObj(Pessoa pessoa) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(pessoa);
    }

    private StringEntity getStringEntityFrom(String conteudoJson) throws UnsupportedEncodingException {
        return new StringEntity(conteudoJson);
    }

    private String convertHttpResponseToString(HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        return convertInputStreamToString(inputStream);
    }

    private String convertInputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String string = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return string;
    }
}
