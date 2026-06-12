package com.lucasscharf.rolador.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Texto de mensagens estendidas (replies, mensagens com link etc.).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExtendedTextMessage(String text) {
}
