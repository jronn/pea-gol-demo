package com.jronn.peaccounting.test.api;

import com.jronn.peaccounting.test.api.request.NextGameStateRequest;
import com.jronn.peaccounting.test.api.response.InitialGameStateResponse;
import com.jronn.peaccounting.test.api.response.NextGameStateResponse;
import com.jronn.peaccounting.test.core.GameStateCalculator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

  private static final int MAX_BOARD_SIZE = 100;

  private final GameStateCalculator gameStateCalculator;

  ApiController(GameStateCalculator gameStateCalculator) {
    this.gameStateCalculator = gameStateCalculator;
  }

  @PostMapping("/nextGameState")
  public NextGameStateResponse nextGameState(@RequestBody NextGameStateRequest request) {
    if (request.state().boardWidth() > MAX_BOARD_SIZE
            || request.state().boardHeight() > MAX_BOARD_SIZE
            || request.state().aliveCells().size() > (MAX_BOARD_SIZE * MAX_BOARD_SIZE)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Board size too large");
    }

    return new NextGameStateResponse(gameStateCalculator.calculateNextState(request.state()));
  }

  @GetMapping("/initialState")
  public InitialGameStateResponse initialState(@RequestParam("width") int boardWidth, @RequestParam("height") int boardHeight) {
    if (boardHeight > MAX_BOARD_SIZE || boardWidth > MAX_BOARD_SIZE) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Board size too large");
    }

    return new InitialGameStateResponse(gameStateCalculator.generateInitialState(boardWidth, boardHeight));
  }
}
