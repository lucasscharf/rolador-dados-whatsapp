package com.lucasscharf.rolador.dice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DiceCommandParserTest {

    private final DiceCommandParser parser = new DiceCommandParser();

    @ParameterizedTest
    @CsvSource({
            "roll 1d6, 1",
            "roll 99d6, 99",
            "ROLL 3D6, 3",
            "'  roll 3d6  ', 3",
            "'roll    5d6', 5"
    })
    void parseValidCommandsReturnsRoll(String message, int expectedQuantity) {
        assertThat(parser.parse(message))
                .isEqualTo(new ParseResult.Roll(expectedQuantity));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "roll 0d6",
            "roll 100d6",
            "roll abc",
            "roll",
            "rollx"
    })
    void parseMalformedRollCommandsReturnsInvalidRoll(String message) {
        assertThat(parser.parse(message))
                .isEqualTo(new ParseResult.InvalidRoll());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "oi",
            "dado 3d6",
            ""
    })
    void parseNonCommandsReturnsNotACommand(String message) {
        assertThat(parser.parse(message))
                .isEqualTo(new ParseResult.NotACommand());
    }

    @Test
    void parseNullReturnsNotACommand() {
        assertThat(parser.parse(null))
                .isEqualTo(new ParseResult.NotACommand());
    }
}
