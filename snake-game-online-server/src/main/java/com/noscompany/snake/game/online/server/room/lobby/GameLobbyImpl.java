package com.noscompany.snake.game.online.server.room.lobby;


import com.noscompany.snake.game.commons.messages.dto.GameLobbyState;
import com.noscompany.snake.game.commons.messages.dto.LobbyAdmin;
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
    private Option<LobbyAdmin> admin;
    private SnakeGameEventHandler snakeGameEventHandler;

    @Override
    synchronized public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userName, SnakeNumber snakeNumberNumber) {
        if (snakeGame.isRunning()) {
            return Either.left(FailedToTakeASeat.gameAlreadyRunning(getLobbyState()));
        } else if (seats.containsValue(snakeNumberNumber)) {
            return Either.left(FailedToTakeASeat.seatAlreadyTaken(getLobbyState()));
        } else {
            seats.remove(userName);
            seats.put(userName, snakeNumberNumber);
            if (thereIsNoAdmin() || adminNameEquals(userName))
                setAsAdmin(userName, snakeNumberNumber);
            createGame();
            return Either.right(new PlayerTookASeat(userName, snakeNumberNumber, getLobbyState()));
        }
    }

    @Override
    public synchronized Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userName) {
        var snakeNumber = seats.remove(userName);
        if (seats.isEmpty()) {
            snakeGame.cancel();
        }
        if (adminNameEquals(userName))
            chooseNewAdmin();
        createGame();
        if (snakeNumber == null)
            return Either.left(FailedToFreeUpSeat.userNotInTheLobby());
        snakeGame.kill(snakeNumber);
        return Either.right(new PlayerFreedUpASeat(userName, snakeNumber, getLobbyState()));
    }

    @Override
    public synchronized Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userName, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        if (snakeGame.isRunning())
            return Either.left(FailedToChangeGameOptions.gameIsAlreadyRunning(getLobbyState()));
        if (!seats.containsKey(userName))
            return Either.left(FailedToChangeGameOptions.requesterDidNotTookASeat(getLobbyState()));
        if (!adminNameEquals(userName))
            return Either.left(FailedToChangeGameOptions.requesterIsNotAdmin(getLobbyState()));
        this.gridSize = gridSize;
        this.gameSpeed = gameSpeed;
        this.walls = walls;
        createGame();
        return Either.right(new GameOptionsChanged(getLobbyState()));
    }

    @Override
    public synchronized Option<FailedToStartGame> startGame(String userName) {
        if (snakeGame.isRunning())
            return Option.of(FailedToStartGame.gameIsAlreadyRunning(getLobbyState()));
        if (!seats.containsKey(userName))
            return Option.of(FailedToStartGame.requesterDidNotTookASeat(getLobbyState()));
        if (!adminNameEquals(userName))
            return Option.of(FailedToStartGame.requesterIsNotAdmin(getLobbyState()));
        createGame().peek(this::start);
        return Option.none();
    }

    @Override
    public synchronized void changeSnakeDirection(String userName, Direction direction) {
        var playerNumber = seats.get(userName);
        if (playerNumber != null) {
            snakeGame.changeSnakeDirection(playerNumber, direction);
        }
    }

    @Override
    public synchronized void cancelGame(String userName) {
        if (adminNameEquals(userName))
            snakeGame.cancel();
    }

    @Override
    public synchronized void cancelGame() {
        snakeGame.cancel();
    }

    @Override
    public synchronized void pauseGame(String userName) {
        if (adminNameEquals(userName))
            snakeGame.pause();
    }

    @Override
    public synchronized void resumeGame(String userName) {
        if (adminNameEquals(userName))
            snakeGame.resume();
    }

    @Override
    public GameLobbyState getLobbyState() {
        return new GameLobbyState(
                gridSize,
                gameSpeed,
                walls,
                admin,
                new HashMap<>(seats),
                snakeGame.isRunning(),
                snakeGame.getGameState());
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

    private void setAsAdmin(String userName, SnakeNumber snakeNumber) {
        admin = Option.of(new LobbyAdmin(userName, snakeNumber));
    }

    private boolean adminNameEquals(String userName) {
        return admin
                .map(LobbyAdmin::getUserName)
                .map(adminName -> adminName.equals(userName))
                .getOrElse(false);
    }

    private Set<SnakeNumber> playerNumbers() {
        return new HashSet<>(seats.values());
    }

    private boolean thereIsNoAdmin() {
        return admin.isEmpty();
    }

    private void removeCurrentAdmin() {
        admin = Option.none();
    }

    private void chooseNewAdmin() {
        removeCurrentAdmin();
        seats
                .entrySet()
                .stream()
                .findAny()
                .ifPresent(entry -> setAsAdmin(entry.getKey(), entry.getValue()));
    }
}