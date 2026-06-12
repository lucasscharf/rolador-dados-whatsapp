package com.lucasscharf.rolador.webhook;

import org.springframework.stereotype.Component;

import com.lucasscharf.rolador.dice.DiceCommandParser;
import com.lucasscharf.rolador.dice.DiceRoller;
import com.lucasscharf.rolador.dice.MessageFormatter;
import com.lucasscharf.rolador.dice.ParseResult;
import com.lucasscharf.rolador.evolution.EvolutionApiClient;

/**
 * Liga o parser de comandos ao rolador de dados e ao envio de respostas.
 */
@Component
public class MessageHandlerImpl implements MessageHandler {

    private final DiceCommandParser parser;
    private final DiceRoller roller;
    private final MessageFormatter formatter;
    private final EvolutionApiClient client;

    public MessageHandlerImpl(DiceCommandParser parser, DiceRoller roller,
            MessageFormatter formatter, EvolutionApiClient client) {
        this.parser = parser;
        this.roller = roller;
        this.formatter = formatter;
        this.client = client;
    }

    @Override
    public void handle(String remoteJid, String text) {
        switch (parser.parse(text)) {
            case ParseResult.Roll(int quantity) ->
                client.sendText(remoteJid, formatter.formatRoll(quantity, roller.roll(quantity)));
            case ParseResult.InvalidRoll() ->
                client.sendText(remoteJid, formatter.formatHelp());
            case ParseResult.NotACommand() -> {
                // Mensagem comum de chat: o bot não responde.
            }
        }
    }
}
