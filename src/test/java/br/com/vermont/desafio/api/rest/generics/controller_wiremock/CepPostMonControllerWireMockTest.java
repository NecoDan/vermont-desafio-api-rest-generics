package br.com.vermont.desafio.api.rest.generics.controller_wiremock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Slf4j
public class CepPostMonControllerWireMockTest {

    private static final String BASE_URL = "localhost";
    private static final String COMPLEMENTO_URL = "api.postmon.com.br/v1/cep/\"";
    private static final int PORT = 8092;

    private static final String THIRD_STATE = "third";
    private static final String SECOND_STATE = "second";
    private static final String TIP_01 = "finally block is not called when System.exit()"
            + " is called in the try block";
    private static final String TIP_02 = "keep your code clean";
    private static final String TIP_03 = "use composition rather than inheritance";
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

        this.wireMockServer = new WireMockServer(PORT);
        this.wireMockServer.start();

        configureFor(BASE_URL, PORT);
        stubFor(get(urlEqualTo("/" + COMPLEMENTO_URL))
                .willReturn(aResponse()
                        .withBody("Welcome to Baeldung!")
                )
        );
    }

    @After
    public void after() {
        if (Objects.nonNull(this.wireMockServer))
            this.wireMockServer.stop();
    }

    @Test
    public void testar() {
        String mensagemInicializacaoWireMock = (Objects.isNull(this.wireMockServer))
                ? "Houve erro ao inicializar server WireMock...."
                : "Server WireMock inicializado com sucesso....";

        Assert.assertNotNull(this.wireMockServer);
        log.info("{}", mensagemInicializacaoWireMock);
    }

//    @Test
//    public void alterarEstadoCadaChamaTeste() throws IOException {
//        createWireMockStub(Scenario.STARTED, SECOND_STATE, TIP_01);
//        createWireMockStub(SECOND_STATE, THIRD_STATE, TIP_02);
//        createWireMockStub(THIRD_STATE, Scenario.STARTED, TIP_03);
//    }
//
//    private void createWireMockStub(String currentState, String nextState, String responseBody) {
//        stubFor(get(urlEqualTo("/java-tip"))
//                .inScenario("java tips")
//                .whenScenarioStateIs(currentState)
//                .willSetStateTo(nextState)
//                .willReturn(aResponse()
//                        .withStatus(200)
//                        .withHeader("Content-Type", TEXT_PLAIN)
//                        .withBody(responseBody)));
//    }
}
