package com.lucasscharf.rolador.dice;

/**
 * Resultado da interpretação de uma mensagem recebida.
 */
public sealed interface ParseResult {

    /** Comando válido: rolar {@code quantity} dados de 6 lados (1 <= quantity <= 99). */
    record Roll(int quantity) implements ParseResult {
    }

    /** A mensagem começa com "roll" mas não é um comando válido. */
    record InvalidRoll() implements ParseResult {
    }

    /** A mensagem não é um comando do bot e deve ser ignorada. */
    record NotACommand() implements ParseResult {
    }
}
