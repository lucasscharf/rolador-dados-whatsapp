package com.lucasscharf.rolador.webhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Recebe eventos de webhook da Evolution API e delega mensagens de texto
 * recebidas ao {@link MessageHandler}.
 */
@RestController
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    private final MessageHandler messageHandler;

    public WebhookController(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @PostMapping({"/webhook", "/webhook/messages-upsert"})
    public void receive(@RequestBody WebhookEvent event) {
        if (!isMessagesUpsert(event.event())) {
            return;
        }
        MessageData data = event.data();
        if (data == null || data.key() == null || data.key().fromMe()) {
            return;
        }
        String text = data.message() != null ? data.message().text() : null;
        if (text == null || text.isBlank()) {
            return;
        }
        try {
            messageHandler.handle(data.key().remoteJid(), text);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem de {}", data.key().remoteJid(), e);
        }
    }

    private boolean isMessagesUpsert(String event) {
        return "messages.upsert".equalsIgnoreCase(event) || "MESSAGES_UPSERT".equalsIgnoreCase(event);
    }
}
