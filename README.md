# Game of life demo app

Built with a Spring Boot backend exposing a rest api with two endpoints. It also serves a very lightweight static html page that 
calls the api and renders it out using a html canvas

# How to build/run

Build & start the app using either maven (requires jdk-19 installed) or docker.
Once started,
Open `localhost:8080` on a somewhat modern browser (js is static and not built with any tooling to allow proper backwards-compatability for older browsers)

### With maven

``
mvn spring-boot:run
``

or

``
mvn clean package && java -jar target/peaccounting.test-0.0.1-SNAPSHOT.jar
``

### With docker
``
docker build -t gol-demo . && docker run -p 8080:8080 gol-demo
``


## API
### /api/v1/initialState
Returns a random initial game state. Sample:
```
curl "localhost:8080/api/v1/initialState?width=2&height=2"
```
```
{
  "state": {
    "aliveCells": [
      {
        "x": 0,
        "y": 1
      },
      {
        "x": 1,
        "y": 0
      }
    ],
    "boardWidth": 2,
    "boardHeight": 2
  }
}
```

### /api/v1/nextGameState
Returns the next generation of game state given a supplied current state. Sample:
```
curl -d '{"state": {"boardWidth": 2, "boardHeight": 2, "aliveCells": [{"x":0, "y":0}]}}' -H 'Content-type: application/json' "localhost:8080/api/v1/nextGameState"
```
```
{
  "nextState": {
    "aliveCells": [],
    "boardWidth": 2,
    "boardHeight": 2
  }
}
```

