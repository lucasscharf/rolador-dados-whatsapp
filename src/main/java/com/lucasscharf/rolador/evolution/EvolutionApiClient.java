package com.lucasscharf.rolador.evolution;

/**
 * Cliente para envio de mensagens via Evolution API.
 */
public interface EvolutionApiClient {

    /**
     * Envia uma mensagem de texto para o chat identificado por {@code number}
     * (o remoteJid completo, ex.: "5511999999999@s.whatsapp.net" ou "...@g.us").
     */
    void sendText(String number, String text);
}
