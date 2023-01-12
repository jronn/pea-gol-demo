package com.jronn.peaccounting.test.core;

import com.jronn.peaccounting.test.core.domain.Cell;
import com.jronn.peaccounting.test.core.domain.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

class GameStateCalculatorTest {

  GameStateCalculator calculator;

  @BeforeEach
  void setup() {
    calculator = new GameStateCalculator();
  }

  @Test
  void givenBoardSizeGeneratesInitialState() {
    int boardWidth = 10;
    int boardHeight = 5;
    GameState initialState = calculator.generateInitialState(boardWidth, boardHeight);
    assertThat(initialState.boardWidth()).isEqualTo(boardWidth);
    assertThat(initialState.boardHeight()).isEqualTo(boardHeight);
  }

  @Test
  void givenEmptyStateReturnsEmptyState() {
    GameState nextState = calculator.calculateNextState(new GameState(List.of(), 1, 1));
    assertThat(nextState).isEqualTo(new GameState(List.of(), 1, 1));
  }

  @Test
  void givenAliveCellWithFewerThanTwoNeighboursCellDies() {
    List<Cell> currentlyAliveCells = List.of(
            cell(0, 0),
            cell(0, 1));
    GameState nextState = calculator.calculateNextState(new GameState(currentlyAliveCells, 2, 2));
    assertThatList(nextState.aliveCells()).isEmpty();
  }

  @Test
  void givenAliveCellWithTwoOrThreeNeighboursCellLives() {
    List<Cell> currentlyAliveCells = new ArrayList<>(List.of(
            cell(0, 0),
            cell(0, 1),
            cell(1, 0)));

    GameState nextState = calculator.calculateNextState(new GameState(currentlyAliveCells, 2, 2));
    assertThatList(nextState.aliveCells()).contains(cell(0, 0));

    currentlyAliveCells.add(cell(1,1));
    nextState = calculator.calculateNextState(new GameState(currentlyAliveCells, 2, 2));
    assertThatList(nextState.aliveCells()).contains(cell(0, 0));
  }

  @Test
  void givenAliveCellWithMoreThenThreeNeighboursCellDies() {
    List<Cell> currentlyAliveCells = new ArrayList<>(
            List.of(cell(1, 1),
                    cell(0, 1),
                    cell(1, 0),
                    cell(2, 1),
                    cell(1, 2)));

    GameState nextState = calculator.calculateNextState(new GameState(currentlyAliveCells, 3, 3));
    assertThatList(nextState.aliveCells()).doesNotContain(cell(1, 1));
  }

  @Test
  void givenDeadCellWithThreeNeighboursCellBecomesAlive() {
    List<Cell> currentlyAliveCells = new ArrayList<>(
            List.of(cell(0, 1),
                    cell(1, 0),
                    cell(1, 1)));

    GameState nextState = calculator.calculateNextState(new GameState(currentlyAliveCells, 2, 2));
    assertThatList(nextState.aliveCells()).contains(cell(0, 0));
  }

  @Test
  void givenKnownRepeatingPatternGivesExpectedState() {
    List<Cell> aliveCellsFirstTick = List.of(
            cell(1, 2),
            cell(2, 2),
            cell(3, 2));

    List<Cell> aliveCellsSecondTick = List.of(
            cell(2, 1),
            cell(2, 2),
            cell(2, 3));

    GameState nextState = calculator.calculateNextState(new GameState(aliveCellsFirstTick, 10, 10));
    assertThatList(nextState.aliveCells()).containsExactlyElementsOf(aliveCellsSecondTick);

    nextState = calculator.calculateNextState(new GameState(aliveCellsSecondTick, 10, 10));
    assertThatList(nextState.aliveCells()).containsExactlyElementsOf(aliveCellsFirstTick);
  }

  private Cell cell(int x, int y) {
    return new Cell(x, y);
  }
}