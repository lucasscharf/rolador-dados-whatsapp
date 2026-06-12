package com.lucasscharf.rolador.webhook;

/**
 * Processa uma mensagem de texto recebida de um chat do WhatsApp.
 */
public interface MessageHandler {

    /**
     * @param remoteJid identificador do chat de origem (contato ou grupo)
     * @param text      conteúdo textual da mensagem
     */
    void handle(String remoteJid, String text);
}
