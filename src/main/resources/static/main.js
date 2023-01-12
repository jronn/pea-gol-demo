let tickRateMs = null;
let boardWidth = null;
let boardHeight = null;

let resetGame = false;
let gameActive = false;

const resizeCanvas = function () {
  const canvas = document.getElementById("gameCanvas");
  canvas.width = window.innerWidth * 0.8;
  canvas.height = window.innerHeight * 0.8;
}

const startGame = function () {
  if (gameActive) {
    resetGame = true;
    // Wait for active game to finish ongoing requests
    setTimeout(startGame, 100);
    return;
  }

  resetGame = false;
  gameActive = true;

  tickRateMs = 1000 / Math.ceil(document.getElementById('tickRate').value);
  boardHeight = document.getElementById('boardHeight').value;
  boardWidth = document.getElementById('boardWidth').value;

  fetch('http://localhost:8080/api/v1/initialState?width=' + boardWidth + '&height=' + boardHeight)
    .then(response => response.json())
    .then(response => {
      if (resetGame) {
        gameActive = false;
        return;
      }

      draw(response.state);

      setTimeout(function () {
        updateState(response.state);
      }, tickRateMs);
    });
}

const updateState = function (previousState) {
  const requestOptions = {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      state: previousState
    })
  };

  fetch('http://localhost:8080/api/v1/nextGameState', requestOptions)
    .then(response => response.json())
    .then(response => {
      if (resetGame) {
        gameActive = false;
        return;
      }

      draw(response.nextState);
      setTimeout(function () {
        updateState(response.nextState);
      }, tickRateMs);
    });
}

const draw = function (state) {
  const canvas = document.getElementById("gameCanvas");
  const context = canvas.getContext("2d");
  context.clearRect(0, 0, canvas.width, canvas.height);
  context.fillStyle = "#3fab58"

  const squareSize = state.boardWidth > state.boardHeight ? canvas.width / state.boardWidth : canvas.height / state.boardHeight;

  state.aliveCells.forEach(cell => context.fillRect(squareSize * cell.x, squareSize * cell.y, squareSize, squareSize));
}

window.addEventListener('resize', resizeCanvas, false);
resizeCanvas();