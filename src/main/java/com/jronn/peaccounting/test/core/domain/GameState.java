package com.jronn.peaccounting.test.core.domain;

import java.util.List;

public record GameState(List<Cell> aliveCells, int boardWidth, int boardHeight) {

  public GameState {
    if (aliveCells == null || boardWidth < 1 || boardHeight < 1) {
      throw new IllegalArgumentException("Invalid game state");
    }

    for (Cell cell : aliveCells) {
      if (cell.x() > (boardWidth - 1) || cell.y() > (boardHeight - 1)) {
        throw new IllegalArgumentException("Cell position out of bounds of game board");
      }
    }
  }

}
