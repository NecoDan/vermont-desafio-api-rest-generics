package br.com.vermont.desafio.api.rest.generics.controller_wiremock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@Slf4j
public class CepPostMonControllerWireMockTest {

    private static final String URL_BASE = "localhost";
    private static final String URL_COMPLEMENTO = "/api/postmon/com/br/v1/cep/";
    private static final String URL_COMPLETA_ACESSO_SERVER = "http://localhost:8092/api/postmon/com/br/v1/cep/";
    private static final int PORT = 8092;

    private static final String THIRD_STATE = "terceiro";
    private static final String SECOND_STATE = "segundo";
    private static final String TIP_01 = "o bloco finally não é chamado em System.exit()"
            + " é chamado no bloco try ";
    private static final String TIP_02 = "mantenha o seu código limpo";
    private static final String TIP_03 = "use composição ao invés de herança";
    private static final String TEXT_PLAIN = "text/plain";

    private WireMockServer wireMockServer;
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

        // instanciando e inicializar o web service
        this.wireMockServer = new WireMockServer(PORT);
        this.wireMockServer.start();

        // definindo configurações e fragmentando o web service
        configureFor(URL_BASE, PORT);
        stubFor(get(urlEqualTo(URL_COMPLEMENTO))
                .willReturn(aResponse()
                        .withBody("Welcome to API CEPS!")
                )
        );
    }

    @After
    public void after() {
        if (Objects.nonNull(this.wireMockServer))
            this.wireMockServer.stop();
    }

    @Test
    public void deveTestarInicializacaoServerInWireMock() {
        log.info("\n#TEST: deveTestarInicializacaoServerInWireMock: ");

        // -- 01_Cenário && -- 02_Ação
        String mensagemInicializacaoWireMock = (Objects.isNull(this.wireMockServer))
                ? "Houve erro ao inicializar server WireMock...."
                : "Server WireMock inicializado com sucesso....";

        // -- 03_Verificação_Validação
        Assert.assertNotNull(this.wireMockServer);
        log.info("{}", mensagemInicializacaoWireMock);
    }

    @Test
    public void deveTestarPrimeiraChamadaServerEmLocalHost() throws IOException {
        log.info("\n#TEST: deveTestarPrimeiraChamadaServerEmLocalHost: ");

        // -- 01_Cenário
        // simulando um cliente se conectando ao server por meio HttpClients
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // -- 02_Ação
        // executando Request e obtendo o Response
        HttpGet request = new HttpGet(URL_COMPLETA_ACESSO_SERVER);
        HttpResponse httpResponse = httpClient.execute(request);

        // -- 03_Verificação_Validação
        // após obter o Response, convertendo em string a resposta
        String strResponse = convertResponseToString(httpResponse);

        verify(getRequestedFor(urlEqualTo(URL_COMPLEMENTO)));
        assertEquals("Welcome to API CEPS!", strResponse);

        log.info("{}", strResponse);
    }

    private String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }

    @Test
    public void alterarEstadoCadaChamaTeste() throws IOException {
        createWireMockStub(Scenario.STARTED, SECOND_STATE, TIP_01);
        createWireMockStub(SECOND_STATE, THIRD_STATE, TIP_02);
        createWireMockStub(THIRD_STATE, Scenario.STARTED, TIP_03);
    }

    private void createWireMockStub(String currentState, String nextState, String responseBody) {
        stubFor(get(urlEqualTo("/java-tip"))
                .inScenario("java tips")
                .whenScenarioStateIs(currentState)
                .willSetStateTo(nextState)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", TEXT_PLAIN)
                        .withBody(responseBody)));
    }
}
