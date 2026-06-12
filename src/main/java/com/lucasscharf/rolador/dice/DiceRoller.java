package com.lucasscharf.rolador.dice;

import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

/**
 * Rola dados de 6 lados usando um {@link RandomGenerator} injetado.
 */
@Component
public class DiceRoller {

    private static final int SIDES = 6;

    private final RandomGenerator randomGenerator;

    public DiceRoller(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    /**
     * Rola {@code quantity} dados de 6 lados.
     *
     * @param quantity quantidade de dados a rolar
     * @return lista com {@code quantity} valores uniformes em [1, 6]
     */
    public List<Integer> roll(int quantity) {
        return IntStream.range(0, quantity)
                .map(i -> randomGenerator.nextInt(1, SIDES + 1))
                .boxed()
                .toList();
    }
}
