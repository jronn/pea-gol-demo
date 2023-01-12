package com.jronn.peaccounting.test.core.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CellTest {

  @Test
  void throwsWhenCreatedWithInvalidArguments() {
    assertThatThrownBy(() -> new Cell(-1, 0)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new Cell(0, -1)).isInstanceOf(IllegalArgumentException.class);
  }
}