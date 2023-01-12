package com.jronn.peaccounting.test.config;

import com.jronn.peaccounting.test.core.GameStateCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  GameStateCalculator gameStateCalculator() {
    return new GameStateCalculator();
  }
}
