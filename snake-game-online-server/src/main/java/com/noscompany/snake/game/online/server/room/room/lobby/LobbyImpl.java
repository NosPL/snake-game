package com.noscompany.snake.game.online.server.room.room.lobby;


import com.noscompany.snake.game.commons.messages.dto.LobbyState;
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

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static io.vavr.control.Option.none;
import static io.vavr.control.Option.of;

@AllArgsConstructor
class LobbyImpl implements Lobby {
    private Map<String, SnakeNumber> seats;
    private GridSize gridSize;
    private GameSpeed gameSpeed;
    private Walls walls;
    private SnakeGameConfiguration snakeGameConfiguration;
    private SnakeGame snakeGame;
    private Option<LobbyAdmin> admin;
    private SnakeGameEventHandler snakeGameEventHandler;

    @Override
    synchronized public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userName, SnakeNumber snakeNumber) {
        if (snakeGame.isRunning()) {
            return left(FailedToTakeASeat.gameAlreadyRunning(getLobbyState()));
        } else if (seats.containsValue(snakeNumber)) {
            return left(FailedToTakeASeat.seatAlreadyTaken(getLobbyState()));
        } else {
            seats.remove(userName);
            seats.put(userName, snakeNumber);
            if (thereIsNoAdmin() || adminNameEquals(userName))
                setAsAdmin(userName, snakeNumber);
            createGame();
            return right(new PlayerTookASeat(userName, snakeNumber, getLobbyState()));
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
            return left(FailedToFreeUpSeat.userNotInTheLobby());
        snakeGame.kill(snakeNumber);
        return right(new PlayerFreedUpASeat(userName, snakeNumber, getLobbyState()));
    }

    @Override
    public synchronized Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userName, GridSize gridSize, GameSpeed gameSpeed, Walls walls) {
        if (snakeGame.isRunning())
            return left(FailedToChangeGameOptions.gameIsAlreadyRunning());
        if (!seats.containsKey(userName))
            return left(FailedToChangeGameOptions.requesterDidNotTookASeat());
        if (!adminNameEquals(userName))
            return left(FailedToChangeGameOptions.requesterIsNotAdmin());
        this.gridSize = gridSize;
        this.gameSpeed = gameSpeed;
        this.walls = walls;
        createGame();
        return right(new GameOptionsChanged(getLobbyState()));
    }

    @Override
    public synchronized Option<FailedToStartGame> startGame(String userName) {
        if (snakeGame.isRunning())
            return of(FailedToStartGame.gameIsAlreadyRunning(getLobbyState()));
        if (!seats.containsKey(userName))
            return of(FailedToStartGame.requesterDidNotTookASeat(getLobbyState()));
        if (!adminNameEquals(userName))
            return of(FailedToStartGame.requesterIsNotAdmin(getLobbyState()));
        createGame().peek(this::start);
        return none();
    }

    @Override
    public void changeSnakeDirection(String userName, Direction direction) {
        var playerNumber = seats.get(userName);
        if (playerNumber != null) {
            snakeGame.changeSnakeDirection(playerNumber, direction);
        }
    }

    @Override
    public void cancelGame(String userName) {
        if (adminNameEquals(userName))
            snakeGame.cancel();
    }

    @Override
    public void pauseGame(String userName) {
        if (adminNameEquals(userName))
            snakeGame.pause();
    }

    @Override
    public void resumeGame(String userName) {
        if (adminNameEquals(userName))
            snakeGame.resume();
    }

    @Override
    public LobbyState getLobbyState() {
        return new LobbyState(
                gridSize,
                gameSpeed,
                walls,
                admin,
                new HashMap<>(seats),
                snakeGame.isRunning(),
                snakeGame.getGameState());
    }

    private Either<SnakeGameConfiguration.Error, SnakeGame> createGame() {
        return snakeGameConfiguration
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
        admin = of(new LobbyAdmin(userName, snakeNumber));
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

    private void chooseNewAdmin() {
        removeCurrentAdmin();
        seats
                .entrySet()
                .stream()
                .findAny()
                .ifPresent(entry -> setAsAdmin(entry.getKey(), entry.getValue()));
    }

    private void removeCurrentAdmin() {
        admin = none();
    }
}