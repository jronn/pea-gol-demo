package com.jronn.peaccounting.test.core.domain;

public record Cell(int x, int y) {
  public Cell {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("Invalid cell position, can't be negative");
    }
  }
}
