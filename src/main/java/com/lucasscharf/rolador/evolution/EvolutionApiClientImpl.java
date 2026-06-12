package com.lucasscharf.rolador.evolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Implementação de {@link EvolutionApiClient} que envia mensagens de texto
 * via {@code POST {baseUrl}/message/sendText/{instance}} da Evolution API v2.
 */
@Component
public class EvolutionApiClientImpl implements EvolutionApiClient {

    private static final Logger log = LoggerFactory.getLogger(EvolutionApiClientImpl.class);

    private final RestClient restClient;
    private final EvolutionProperties properties;

    public EvolutionApiClientImpl(RestClient.Builder restClientBuilder, EvolutionProperties properties) {
        this.restClient = restClientBuilder.build();
        this.properties = properties;
    }

    @Override
    public void sendText(String number, String text) {
        try {
            restClient.post()
                    .uri(properties.baseUrl() + "/message/sendText/" + properties.instance())
                    .header("apikey", properties.apiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new SendTextRequest(number, text))
                    .retrieve()
                    .toBodilessEntity();
            log.info("Mensagem de texto enviada para {}", number);
        } catch (RestClientException e) {
            log.error("Falha ao enviar mensagem de texto para {}", number, e);
        }
    }

    /** Body do endpoint sendText da Evolution API v2. */
    record SendTextRequest(String number, String text) {
    }
}
