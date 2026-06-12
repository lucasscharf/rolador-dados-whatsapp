package com.lucasscharf.rolador.dice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class MessageFormatterTest {

    private final MessageFormatter formatter = new MessageFormatter();

    @Test
    void formatRollWithThreeDice() {
        assertThat(formatter.formatRoll(3, List.of(4, 2, 6)))
                .isEqualTo("🎲 3d6: 4, 2, 6");
    }

    @Test
    void formatRollWithSingleDie() {
        assertThat(formatter.formatRoll(1, List.of(5)))
                .isEqualTo("🎲 1d6: 5");
    }

    @Test
    void formatHelpReturnsUsageMessage() {
        assertThat(formatter.formatHelp())
                .isEqualTo("Comando inválido. Use: roll Xd6 (X entre 1 e 99). Exemplo: roll 3d6");
    }
}
