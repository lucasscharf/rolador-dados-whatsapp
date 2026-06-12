package com.lucasscharf.rolador.dice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Interpreta mensagens recebidas e identifica comandos de rolagem de dados.
 */
@Component
public class DiceCommandParser {

    private static final Pattern ROLL_PATTERN =
            Pattern.compile("^roll\\s+(\\d+)d6$", Pattern.CASE_INSENSITIVE);

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 99;

    /**
     * Interpreta a mensagem e retorna o resultado da análise.
     *
     * @param message mensagem recebida (pode ser nula)
     * @return {@link ParseResult.Roll} para comandos válidos,
     *         {@link ParseResult.InvalidRoll} para comandos malformados,
     *         {@link ParseResult.NotACommand} para mensagens que não são comandos
     */
    public ParseResult parse(String message) {
        if (message == null) {
            return new ParseResult.NotACommand();
        }

        String trimmed = message.trim();
        Matcher matcher = ROLL_PATTERN.matcher(trimmed);
        if (matcher.matches()) {
            int quantity;
            try {
                quantity = Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                // Número grande demais para int: comando malformado.
                return new ParseResult.InvalidRoll();
            }
            if (quantity >= MIN_QUANTITY && quantity <= MAX_QUANTITY) {
                return new ParseResult.Roll(quantity);
            }
            return new ParseResult.InvalidRoll();
        }

        if (startsWithRoll(trimmed)) {
            return new ParseResult.InvalidRoll();
        }

        return new ParseResult.NotACommand();
    }

    private boolean startsWithRoll(String trimmed) {
        return trimmed.regionMatches(true, 0, "roll", 0, 4);
    }
}
