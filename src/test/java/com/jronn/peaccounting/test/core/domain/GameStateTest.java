package com.jronn.peaccounting.test.core.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class GameStateTest {

  @Test
  void throwsWhenCreatedWithInvalidArguments() {
    assertThatThrownBy(() -> new GameState(null, 10, 10)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new GameState(List.of(), -1, 10)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new GameState(List.of(), 10, -1)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new GameState(List.of(new Cell(10, 2)), 10, 10)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new GameState(List.of(new Cell(2, 10)), 10, 10)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new GameState(List.of(new Cell(100, 100)), 10, 10)).isInstanceOf(IllegalArgumentException.class);
  }
}