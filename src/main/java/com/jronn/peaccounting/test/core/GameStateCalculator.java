package com.jronn.peaccounting.test.core;

import com.jronn.peaccounting.test.core.domain.Cell;
import com.jronn.peaccounting.test.core.domain.GameState;

import java.util.ArrayList;
import java.util.List;

public class GameStateCalculator {

  public GameState generateInitialState(int boardWidth, int boardHeight) {
    List<Cell> aliveCells = new ArrayList<>();

    for (int x = 0; x < boardWidth; x++) {
      for (int y = 0; y < boardHeight; y++) {
        if (Math.random() > 0.5) {
          aliveCells.add(new Cell(x, y));
        }
      }
    }

    return new GameState(aliveCells, boardWidth, boardHeight);
  }

  public GameState calculateNextState(GameState previousState) {
    boolean[][] previousGameBoard = constructGameBoard(previousState);
    boolean[][] nextGameBoard = incrementGameCycle(previousGameBoard);

    List<Cell> aliveCells = getAliveCellsInBoard(nextGameBoard);
    return new GameState(aliveCells, previousState.boardWidth(), previousState.boardHeight());
  }

  private boolean[][] constructGameBoard(GameState state) {
    boolean[][] gameBoard = new boolean[state.boardWidth()][state.boardHeight()];

    for (Cell aliveCell : state.aliveCells()) {
      gameBoard[aliveCell.x()][aliveCell.y()] = true;
    }

    return gameBoard;
  }

  private boolean[][] incrementGameCycle(boolean[][] previousGameBoard) {
    boolean[][] newGameBoard = new boolean[previousGameBoard.length][previousGameBoard.length == 0 ? 0 : previousGameBoard[0].length];

    for (int x = 0; x < previousGameBoard.length; x++) {
      for (int y = 0; y < previousGameBoard[x].length; y++) {
        boolean currentCellAlive = previousGameBoard[x][y];
        int aliveNeighbours = getNumberOfAliveCellNeighbours(x, y, previousGameBoard);

        if ((currentCellAlive && (aliveNeighbours == 2 || aliveNeighbours == 3)) || (!currentCellAlive && aliveNeighbours == 3)) {
          newGameBoard[x][y] = true;
        }
      }
    }

    return newGameBoard;
  }

  private int getNumberOfAliveCellNeighbours(int cellPosX, int cellPosY, boolean[][] board) {
    int aliveNeighbours = 0;

    for (int x = cellPosX - 1; x <= (cellPosX + 1); x++) {
      for (int y = cellPosY - 1; y <= (cellPosY + 1); y++) {
        if (!((x == cellPosX) && (y == cellPosY))) {

          if (positionIsWithinGrid(x, y, board) && board[x][y]) {
            aliveNeighbours++;
          }
        }
      }
    }

    return aliveNeighbours;
  }

  private boolean positionIsWithinGrid(int x, int y, boolean[][] grid) {
    return x >= 0 && x < grid.length && y >= 0 && y < grid[x].length;
  }

  private List<Cell> getAliveCellsInBoard(boolean[][] board) {
    List<Cell> aliveCells = new ArrayList<>();
    for (int x = 0; x < board.length; x++) {
      for (int y = 0; y < board[x].length; y++) {
        if (board[x][y]) {
          aliveCells.add(new Cell(x, y));
        }
      }
    }
    return aliveCells;
  }
}
