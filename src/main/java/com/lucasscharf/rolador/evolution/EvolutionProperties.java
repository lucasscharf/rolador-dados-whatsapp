package com.lucasscharf.rolador.evolution;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configurações de acesso à Evolution API.
 *
 * @param baseUrl  URL base da Evolution API (ex.: http://localhost:8081)
 * @param apiKey   chave enviada no header {@code apikey}
 * @param instance nome da instância da Evolution API
 */
@ConfigurationProperties("evolution")
public record EvolutionProperties(String baseUrl, String apiKey, String instance) {
}
