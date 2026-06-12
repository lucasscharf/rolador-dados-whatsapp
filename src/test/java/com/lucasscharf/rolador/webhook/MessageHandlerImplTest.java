package com.lucasscharf.rolador.webhook;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lucasscharf.rolador.dice.DiceCommandParser;
import com.lucasscharf.rolador.dice.DiceRoller;
import com.lucasscharf.rolador.dice.MessageFormatter;
import com.lucasscharf.rolador.evolution.EvolutionApiClient;

class MessageHandlerImplTest {

    private static final String JID = "5511999999999@s.whatsapp.net";

    private DiceRoller roller;
    private EvolutionApiClient client;
    private MessageHandlerImpl handler;

    @BeforeEach
    void setUp() {
        roller = mock(DiceRoller.class);
        client = mock(EvolutionApiClient.class);
        handler = new MessageHandlerImpl(new DiceCommandParser(), roller,
                new MessageFormatter(), client);
    }

    @Test
    void respondeComRolagensETotalParaComandoValido() {
        when(roller.roll(3)).thenReturn(List.of(4, 2, 6));

        handler.handle(JID, "roll 3d6");

        verify(client).sendText(JID, "🎲 3d6: 4, 2, 6");
    }

    @Test
    void respondeComAjudaParaComandoInvalido() {
        handler.handle(JID, "roll 200d6");

        verify(client).sendText(JID, new MessageFormatter().formatHelp());
        verifyNoInteractions(roller);
    }

    @Test
    void ignoraMensagemQueNaoEComando() {
        handler.handle(JID, "bom dia, grupo");

        verifyNoInteractions(roller, client);
    }

    @Test
    void fluxoCompletoComRollerRealGeraValoresValidos() {
        DiceRoller realRoller = new DiceRoller(new Random(42));
        MessageHandlerImpl realHandler = new MessageHandlerImpl(new DiceCommandParser(),
                realRoller, new MessageFormatter(), client);

        realHandler.handle(JID, "roll 5d6");

        verify(client).sendText(org.mockito.ArgumentMatchers.eq(JID),
                org.mockito.ArgumentMatchers.matches("🎲 5d6: [1-6], [1-6], [1-6], [1-6], [1-6]"));
    }
}
