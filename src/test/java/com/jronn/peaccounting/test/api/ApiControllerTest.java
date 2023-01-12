package com.jronn.peaccounting.test.api;

import com.jronn.peaccounting.test.core.GameStateCalculator;
import com.jronn.peaccounting.test.core.domain.Cell;
import com.jronn.peaccounting.test.core.domain.GameState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiController.class)
class ApiControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private GameStateCalculator gameStateCalculator;

  @Test
  public void getNextGameState() throws Exception {
    when(gameStateCalculator.calculateNextState(any())).thenReturn(new GameState(List.of(new Cell(0, 0)), 5, 6));

    mvc.perform(createNextGameStateRequest(List.of(new Cell(0, 0), new Cell(0, 1), new Cell(0, 2)), 3, 3))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.nextState").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.nextState.aliveCells", hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nextState.aliveCells[0].x", is(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nextState.aliveCells[0].y", is(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nextState.boardWidth", is(5)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nextState.boardHeight", is(6)));
  }

  @Test
  public void nextGameStateReturnsBadRequestWhenBoardSizeTooLarge() throws Exception {
    mvc.perform(createNextGameStateRequest(List.of(), 101, 3))
            .andExpect(status().isBadRequest());

    mvc.perform(createNextGameStateRequest(List.of(), 3, 101))
            .andExpect(status().isBadRequest());
  }

  @Test
  public void getInitialGameState() throws Exception {
    when(gameStateCalculator.generateInitialState(3, 3)).thenReturn(new GameState(List.of(), 3, 3));

    mvc.perform(createInitialStateRequest(3, 3))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.state").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.state.aliveCells", hasSize(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.state.boardWidth", is(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.state.boardHeight", is(3)));
  }

  @Test
  public void initialStateReturnsBadRequestWhenBoardSizeTooLarge() throws Exception {
    mvc.perform(createInitialStateRequest(101, 3))
            .andExpect(status().isBadRequest());

    mvc.perform(createInitialStateRequest(3, 101))
            .andExpect(status().isBadRequest());
  }

  private MockHttpServletRequestBuilder createNextGameStateRequest(List<Cell> aliveCells, int width, int height) {
    String requestBodyTemplate = """
            {
              "state": {
                "aliveCells": [%s],
                "boardWidth": %d,
                "boardHeight": %d
              }
            }
            """;

    String aliveCellsString = aliveCells.stream().map(c -> String.format("""
                    { "x": %d, "y": %d }""", c.x(), c.y()))
            .collect(Collectors.joining(","));

    return post("/api/v1/nextGameState")
            .content(String.format(requestBodyTemplate, aliveCellsString, width, height))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
  }

  private MockHttpServletRequestBuilder createInitialStateRequest(int width, int height) {
    return get("/api/v1/initialState")
            .param("width", Integer.toString(width))
            .param("height", Integer.toString(height))
            .accept(MediaType.APPLICATION_JSON);
  }
}