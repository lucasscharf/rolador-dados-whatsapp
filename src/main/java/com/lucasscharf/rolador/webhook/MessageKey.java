package com.lucasscharf.rolador.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Chave identificadora da mensagem no WhatsApp.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageKey(String remoteJid, boolean fromMe) {
}
