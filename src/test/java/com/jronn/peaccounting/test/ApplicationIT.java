package com.jronn.peaccounting.test;

import com.jronn.peaccounting.test.api.request.NextGameStateRequest;
import com.jronn.peaccounting.test.api.response.InitialGameStateResponse;
import com.jronn.peaccounting.test.api.response.NextGameStateResponse;
import com.jronn.peaccounting.test.core.domain.Cell;
import com.jronn.peaccounting.test.core.domain.GameState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIT {

  @Autowired
  TestRestTemplate testRestTemplate;

  @Test
  void givenCurrentGameStateReturnsNextState() {
    int boardWidth = 5;
    int boardHeight = 5;

    List<Cell> currentlyAliveCells = List.of(
            new Cell(1, 2),
            new Cell(2, 2),
            new Cell(3, 2)
    );

    var requestEntity = new HttpEntity<>(new NextGameStateRequest(new GameState(currentlyAliveCells, boardWidth, boardHeight)));
    ResponseEntity<NextGameStateResponse> response = testRestTemplate.postForEntity("/api/v1/nextGameState", requestEntity, NextGameStateResponse.class);

    assertThat(response.getStatusCode().value()).isEqualTo(200);
    assertThat(response.getBody().nextState().boardHeight()).isEqualTo(boardHeight);
    assertThat(response.getBody().nextState().boardWidth()).isEqualTo(boardWidth);

    assertThatList(response.getBody().nextState().aliveCells()).containsExactly(
            new Cell(2, 1),
            new Cell(2, 2),
            new Cell(2, 3)
    );
  }

  @Test
  void givenBoardSizeGeneratesInitialState() {
    int boardWidth = 5;
    int boardHeight = 5;

    ResponseEntity<InitialGameStateResponse> response = testRestTemplate.getForEntity("/api/v1/initialState?width={width}&height={height}", InitialGameStateResponse.class,
            Map.of("width", boardWidth,
                    "height", boardHeight));

    assertThat(response.getStatusCode().value()).isEqualTo(200);
    assertThat(response.getBody().state().boardHeight()).isEqualTo(boardHeight);
    assertThat(response.getBody().state().boardWidth()).isEqualTo(boardWidth);
  }
}
