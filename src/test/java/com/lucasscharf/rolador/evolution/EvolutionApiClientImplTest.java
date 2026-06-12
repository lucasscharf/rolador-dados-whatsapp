package com.lucasscharf.rolador.evolution;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class EvolutionApiClientImplTest {

    private MockRestServiceServer server;
    private EvolutionApiClientImpl client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        EvolutionProperties properties = new EvolutionProperties("http://localhost:8081", "minha-chave", "bot");
        client = new EvolutionApiClientImpl(builder, properties);
    }

    @Test
    void deveEnviarTextoComUrlHeaderEBodyCorretos() {
        server.expect(requestTo("http://localhost:8081/message/sendText/bot"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("apikey", "minha-chave"))
                .andExpect(content().json("""
                        {"number": "5511999999999@s.whatsapp.net", "text": "Resultado: 12"}
                        """))
                .andRespond(withSuccess());

        client.sendText("5511999999999@s.whatsapp.net", "Resultado: 12");

        server.verify();
    }

    @Test
    void naoDevePropagarExcecaoQuandoApiRetornaErro500() {
        server.expect(requestTo("http://localhost:8081/message/sendText/bot"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());

        assertThatCode(() -> client.sendText("5511999999999@s.whatsapp.net", "oi"))
                .doesNotThrowAnyException();

        server.verify();
    }
}
