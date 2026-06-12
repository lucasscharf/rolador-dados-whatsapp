package com.lucasscharf.rolador.dice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

class DiceRollerTest {

    private final DiceRoller roller = new DiceRoller(new Random(42));

    @Test
    void rollOneDieReturnsSingleValueBetweenOneAndSix() {
        List<Integer> rolls = roller.roll(1);

        assertThat(rolls).hasSize(1);
        assertThat(rolls).allSatisfy(value -> assertThat(value).isBetween(1, 6));
    }

    @Test
    void rollNinetyNineDiceReturnsAllValuesBetweenOneAndSix() {
        List<Integer> rolls = roller.roll(99);

        assertThat(rolls).hasSize(99);
        assertThat(rolls).allSatisfy(value -> assertThat(value).isBetween(1, 6));
    }
}
