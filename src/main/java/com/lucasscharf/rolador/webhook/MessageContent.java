package com.lucasscharf.rolador.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Conteúdo da mensagem: texto simples chega em {@code conversation};
 * replies e mensagens com link chegam em {@code extendedTextMessage.text}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageContent(String conversation, ExtendedTextMessage extendedTextMessage) {

    /**
     * Retorna o texto da mensagem, vindo de {@code conversation} ou, se nulo,
     * de {@code extendedTextMessage.text()}; pode ser {@code null}.
     */
    public String text() {
        if (conversation != null) {
            return conversation;
        }
        return extendedTextMessage != null ? extendedTextMessage.text() : null;
    }
}
