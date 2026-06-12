package com.lucasscharf.rolador.webhook;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WebhookController.class)
class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageHandler messageHandler;

    private static final String PAYLOAD_CONVERSATION = """
            {
              "event": "messages.upsert",
              "instance": "minha-instancia",
              "data": {
                "key": {"remoteJid": "5511999999999@s.whatsapp.net", "fromMe": false, "id": "ABC123"},
                "pushName": "João",
                "message": {"conversation": "roll 3d6"},
                "messageType": "conversation"
              },
              "server_url": "http://evolution:8080",
              "apikey": "chave"
            }
            """;

    @Test
    void deveChamarHandlerComPayloadValidoDeConversation() throws Exception {
        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_CONVERSATION))
                .andExpect(status().isOk());

        verify(messageHandler).handle("5511999999999@s.whatsapp.net", "roll 3d6");
    }

    @Test
    void deveChamarHandlerComExtendedTextMessage() throws Exception {
        String payload = """
                {
                  "event": "messages.upsert",
                  "instance": "minha-instancia",
                  "data": {
                    "key": {"remoteJid": "5511888888888@s.whatsapp.net", "fromMe": false, "id": "DEF456"},
                    "pushName": "Maria",
                    "message": {"extendedTextMessage": {"text": "roll 1d20"}},
                    "messageType": "extendedTextMessage"
                  },
                  "server_url": "http://evolution:8080",
                  "apikey": "chave"
                }
                """;

        mockMvc.perform(post("/webhook/messages-upsert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        verify(messageHandler).handle("5511888888888@s.whatsapp.net", "roll 1d20");
    }

    @Test
    void naoDeveChamarHandlerQuandoFromMeForTrue() throws Exception {
        String payload = """
                {
                  "event": "messages.upsert",
                  "instance": "minha-instancia",
                  "data": {
                    "key": {"remoteJid": "5511999999999@s.whatsapp.net", "fromMe": true, "id": "ABC123"},
                    "pushName": "João",
                    "message": {"conversation": "roll 3d6"},
                    "messageType": "conversation"
                  }
                }
                """;

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        verifyNoInteractions(messageHandler);
    }

    @Test
    void naoDeveChamarHandlerParaEventoDiferente() throws Exception {
        String payload = """
                {
                  "event": "connection.update",
                  "instance": "minha-instancia",
                  "data": {
                    "key": {"remoteJid": "5511999999999@s.whatsapp.net", "fromMe": false, "id": "ABC123"},
                    "message": {"conversation": "roll 3d6"}
                  }
                }
                """;

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        verifyNoInteractions(messageHandler);
    }

    @Test
    void naoDeveChamarHandlerQuandoTextoEstiverAusente() throws Exception {
        String payload = """
                {
                  "event": "messages.upsert",
                  "instance": "minha-instancia",
                  "data": {
                    "key": {"remoteJid": "5511999999999@s.whatsapp.net", "fromMe": false, "id": "ABC123"},
                    "pushName": "João",
                    "message": {"imageMessage": {"url": "http://exemplo.com/foto.jpg"}},
                    "messageType": "imageMessage"
                  }
                }
                """;

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        verifyNoInteractions(messageHandler);
    }

    @Test
    void deveResponder200QuandoHandlerLancarExcecao() throws Exception {
        doThrow(new RuntimeException("falha no handler"))
                .when(messageHandler).handle(anyString(), anyString());

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PAYLOAD_CONVERSATION))
                .andExpect(status().isOk());

        verify(messageHandler).handle("5511999999999@s.whatsapp.net", "roll 3d6");
    }
}
