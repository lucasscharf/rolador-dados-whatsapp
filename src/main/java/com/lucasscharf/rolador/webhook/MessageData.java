package com.lucasscharf.rolador.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Dados da mensagem em um evento {@code messages.upsert}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageData(MessageKey key, String pushName, MessageContent message) {
}
