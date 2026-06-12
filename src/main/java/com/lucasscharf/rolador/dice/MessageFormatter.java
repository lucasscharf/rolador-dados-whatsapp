package com.lucasscharf.rolador.dice;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
 * Formata as mensagens de resposta do bot.
 */
@Component
public class MessageFormatter {

    /**
     * Formata o resultado de uma rolagem, por exemplo:
     * {@code 🎲 3d6: 4, 2, 6}.
     *
     * @param quantity quantidade de dados rolados
     * @param rolls    valores obtidos
     * @return mensagem formatada
     */
    public String formatRoll(int quantity, List<Integer> rolls) {
        String values = rolls.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        int total = rolls.stream().mapToInt(Integer::intValue).sum();
        return "🎲 %dd6: %s".formatted(quantity, values);
    }

    /**
     * Mensagem de ajuda para comandos inválidos.
     *
     * @return texto explicando a sintaxe do comando
     */
    public String formatHelp() {
        return "Comando inválido. Use: roll Xd6 (X entre 1 e 99). Exemplo: roll 3d6";
    }
}
