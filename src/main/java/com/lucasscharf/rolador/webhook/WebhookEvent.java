package com.lucasscharf.rolador.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Evento recebido no webhook da Evolution API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record WebhookEvent(String event, String instance, MessageData data) {
}
