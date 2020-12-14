package br.com.vermont.desafio.api.rest.generics.enderecos.controller;

import br.com.vermont.desafio.api.rest.generics.enderecos.model.Endereco;
import br.com.vermont.desafio.api.rest.generics.util.FormatterUtil;
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
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(CepPostMonController.class)
public class ApiWebAcessPublicControllerTest {

    public static final String BASE_URL_EXTERNA = CepPostMonController.BASE_URL;
    private static final String BASE_URL = "/enderecos/";

    @Autowired
    private MockMvc mockMvc;

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
    public void deveRetornarPeloMenosUnicoEnderecoCompletoPorNumeroCepMethodGET() throws Exception {
        log.info("\n#TEST: deveRetornarPeloMenosUnicoEnderecoCompletoPorNumeroCepMethodGET: ");

        // -- 01_Cenário
        String cep = "65110-000";
        String uriExterna = BASE_URL_EXTERNA.concat("/").concat(cep);

        // -- 02_Ação
        RestTemplate restTemplate = new RestTemplate();
        Endereco enderecoParam = restTemplate.getForObject(uriExterna, Endereco.class);

        String uri = BASE_URL.concat(cep);
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        if (Objects.nonNull(enderecoParam)) {
            response.andExpect(status()
                    .isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.cep").exists())
                    .andExpect(jsonPath("$.cidade").exists())
                    .andExpect(jsonPath("$.estado").exists())
                    .andExpect(jsonPath("$.cep").isNotEmpty())
                    .andExpect(jsonPath("$.cidade").isNotEmpty())
                    .andExpect(jsonPath("$.estado").isNotEmpty())
                    .andExpect(jsonPath("$.cep").value(FormatterUtil.retirarCaracteresNaoNumericos(cep)))
                    .andExpect(jsonPath("$.cidade").value(enderecoParam.getCidade()))
                    .andExpect(jsonPath("$.estado").value(enderecoParam.getEstado()));
        }

        assertNotNull(response.andReturn().getResponse().getContentAsString());
        toStringEnd(response, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarPeloMenosUnicoEnderecoCompletoPorNumeroCepMethodGETNotMock() throws Exception {
        log.info("\n#TEST: deveRetornarPeloMenosUnicoEnderecoCompletoPorNumeroCepMethodGETNotMock: ");

        // -- 01_Cenário
        String cep = "65110-000";

        // -- 02_Ação
        String uri = BASE_URL_EXTERNA.concat("/").concat(cep);

        RestTemplate restTemplate = new RestTemplate();
        Endereco endereco = restTemplate.getForObject(uri, Endereco.class);

        // -- 03_Verificação_Validação
        assertTrue(Objects.nonNull(endereco) && endereco.getCep().equals(FormatterUtil.retirarCaracteresNaoNumericos(cep)));
        toStringEndGetConteudoJSON(getJsonValueEnderecoFromPessoaObj(endereco));
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

    private String getJsonValueEnderecoFromPessoaObj(Endereco endereco) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(endereco);
    }

    private void toStringEndGetConteudoJSON(String strConteudoJSON) throws Exception {
        if (Objects.isNull(strConteudoJSON)) {
            log.info("#TEST_RESULT: ".concat("Error ao gerar saida. Não existem dados..."));
            log.info("-------------------------------------------------------------");
            return;
        }

        String out = StringUtil.formatConteudoJSONFrom(strConteudoJSON);
        log.info("#TEST_RESULT: ".concat(out));
        log.info("-------------------------------------------------------------");
    }
}
