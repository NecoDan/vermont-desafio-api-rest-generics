package br.com.vermont.desafio.api.rest.generics.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.Builder;
import lombok.Data;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TesteGetUsingMock {

    /**
     * 1. Criar o servidor Wiremock e startar
     * 2. Configurar o servidor para interceptar as requisições
     * 3. Stub para as requisições [get request]
     * - Criar o mock do response
     * 4. Shutdown no servidor WireMock
     */
    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    private static WireMockServer server;

    private static final String BASE_URL = "/perfis/";
    private static final String CEP_DEFAULT = "65110-000";


    @BeforeClass
    public static void setup() throws JsonProcessingException {
        server = new WireMockServer(PORT);
        server.start();

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        Perfil perfilMockResponse = Perfil.builder().descricao("Advogado de Cadastro Trabalhista").token("03624e47-61d3-44ff-960f-c6b3e8a06cef").build();
        String json = objectMapper.writeValueAsString(perfilMockResponse);

        ResponseDefinitionBuilder mockResponse = new ResponseDefinitionBuilder();
        mockResponse.withStatus(HttpStatus.SC_OK)
                .withBody(json)
                .withHeader("Content-Type", "application/json");

        configureFor(HOST, PORT); // http://localhost:8080
        stubFor(
                WireMock.get(urlPathEqualTo(BASE_URL))
                        .willReturn(mockResponse)
        );
    }

    @Test
    public void getEndPoint() throws URISyntaxException {
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get(new URI(BASE_URL))
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
//                .and()
//                .body("[0].descricao", equalTo("Advogado de Cadastro Trabalhista"));
    }

    @AfterClass
    public static void teardown() {
        if (Objects.nonNull(server) && server.isRunning()) {
            server.shutdownServer();
        }
    }

    @Data
    @Builder
    static class Perfil {
        private String descricao;
        private String token;
    }
}
