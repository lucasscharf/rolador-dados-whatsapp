package com.lucasscharf.rolador.dice;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de beans do domínio de dados.
 */
@Configuration
public class DiceConfig {

    @Bean
    RandomGenerator randomGenerator() {
        return new SecureRandom();
    }
}
