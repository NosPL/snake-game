package com.noscompany.snake.game.server.lobby;


import com.noscompany.snake.game.commons.messages.events.lobby.*;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.core.SnakeGame;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.SnakeGameEventHandler;
import snake.game.core.dto.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
class GameLobbyImpl implements GameLobby {
    private Map<String, SnakeNumber> seats;
    private GridSize gridSize;
    private GameSpeed gameSpeed;
    private Walls walls;
    private SnakeGame snakeGame;
    private Option<String> adminOp;
    private SnakeGameEventHandler snakeGameEventHandler;

    @Override
    synchronized public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userId, SnakeNumber snakeNumberNumber) {
        if (snakeGame.isRunning()) {
            return Either.left(FailedToTakeASeat.gameAlreadyRunning());
        } else if (seats.containsValue(snakeNumberNumber)) {
            return Either.left(FailedToTakeASeat.seatAlreadyTaken());
        } else {
            seats.remove(userId);
            seats.put(userId, snakeNumberNumber);
            if (thereIsNoAdmin())
                setAsAdmin(userId);
            createGame();
            return Either.right(new PlayerTookASeat(userId, snakeNumberNumber, getLobbyState()));
        }
    }

    @Override
    public synchronized Option<PlayerFreedUpASeat> freeUpASeat(String userId) {
        var snakeNumber = seats.remove(userId);
        if (seats.isEmpty()) {
            snakeGame.cancel();
        }
        if (adminIdEquals(userId))
            chooseNewAdmin();
        createGame();
        if (snakeNumber == null)
            return Option.none();
        return Option.of(new PlayerFreedUpASeat(userId, snakeNumber, getLobbyState()));
    }

    @Override
    public synchronized Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userId, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        if (snakeGame.isRunning())
            return Either.left(FailedToChangeGameOptions.gameIsAlreadyRunning(this.gridSize, this.gameSpeed, this.walls));
        if (!seats.containsKey(userId))
            return Either.left(FailedToChangeGameOptions.requesterDidNotTookASeat(this.gridSize, this.gameSpeed, this.walls));
        if (!adminIdEquals(userId))
            return Either.left(FailedToChangeGameOptions.requesterIsNotAdmin(this.gridSize, this.gameSpeed, this.walls));
        this.gridSize = gridSize;
        this.gameSpeed = gameSpeed;
        this.walls = walls;
        createGame();
        return Either.right(new GameOptionsChanged(gridSize, gameSpeed, walls, getLobbyState()));
    }

    @Override
    public synchronized Option<FailedToStartGame> startGame(String userId) {
        if (snakeGame.isRunning())
            return Option.of(FailedToStartGame.gameIsAlreadyRunning());
        if (!seats.containsKey(userId))
            return Option.of(FailedToStartGame.requesterDidNotTookASeat());
        if (!adminIdEquals(userId))
            return Option.of(FailedToStartGame.requesterIsNotAdmin());
        createGame().peek(this::start);
        return Option.none();
    }

    @Override
    public synchronized void changeSnakeDirection(String userId, Direction direction) {
        var playerNumber = seats.get(userId);
        if (playerNumber != null) {
            snakeGame.changeSnakeDirection(playerNumber, direction);
        }
    }

    @Override
    public synchronized void cancelGame(String userId) {
        if (adminIdEquals(userId))
            snakeGame.cancel();
    }

    @Override
    public synchronized void cancelGame() {
        snakeGame.cancel();
    }

    @Override
    public synchronized void pauseGame(String userId) {
        if (adminIdEquals(userId))
            snakeGame.pause();
    }

    @Override
    public synchronized void resumeGame(String userId) {
        if (adminIdEquals(userId))
            snakeGame.resume();
    }

    @Override
    public synchronized GameLobbyState getLobbyState() {
        return new GameLobbyState(
                adminOp.getOrNull(),
                new HashMap<>(seats),
                snakeGame.isRunning(),
                toDto(snakeGame.getGameState()));
    }

    private GameStateDto toDto(GameState gameState) {
        return GameStateDto.asDto(gameState);
    }

    @Override
    public Option<PlayerFreedUpASeat> userLeft(String userId) {
        final SnakeNumber snakeNumber = seats.remove(userId);
        if (adminIdEquals(userId))
            removeCurrentAdmin();
        if (seats.isEmpty())
            snakeGame.cancel();
        if (snakeNumber == null)
            return Option.none();
        else {
            createGame();
            return Option.of(new PlayerFreedUpASeat(userId, snakeNumber, getLobbyState()));
        }
    }

    private Either<SnakeGameConfiguration.Error, SnakeGame> createGame() {
        return new SnakeGameConfiguration()
                .set(walls)
                .set(gridSize)
                .set(gameSpeed)
                .set(playerNumbers())
                .set(snakeGameEventHandler)
                .set(CountdownTime.inSeconds(3))
                .create()
                .peek(this::setGame)
                .peekLeft(this::handleError);
    }

    private void handleError(SnakeGameConfiguration.Error error) {
        this.snakeGame = new NullGame(gridSize);
    }

    private void setGame(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
    }

    private void start(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
        this.snakeGame.start();
    }

    private void setAsAdmin(String s) {
        adminOp = Option.of(s);
    }

    private boolean adminIdEquals(String userId) {
        return adminOp
                .map(adminId -> adminId.equals(userId))
                .getOrElse(false);
    }

    private Set<SnakeNumber> playerNumbers() {
        return new HashSet<>(seats.values());
    }

    private boolean thereIsNoAdmin() {
        return adminOp.isEmpty();
    }

    private void removeCurrentAdmin() {
        adminOp = Option.none();
    }

    private void chooseNewAdmin() {
        removeCurrentAdmin();
        seats
                .keySet().stream()
                .findAny()
                .ifPresent(this::setAsAdmin);
    }
}