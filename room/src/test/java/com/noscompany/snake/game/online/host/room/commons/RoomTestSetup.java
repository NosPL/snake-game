package com.noscompany.snake.game.online.host.room.commons;

import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState.Seat;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snake.game.online.host.room.Room;
import com.noscompany.snake.game.online.host.room.RoomConfiguration;
import com.noscompany.snake.game.online.host.room.dto.UserId;
import io.vavr.control.Either;
import org.junit.Before;

import java.util.UUID;
import java.util.stream.Stream;

public class RoomTestSetup {
    protected int userIdCounter;
    protected int userNameCounter;
    protected Room room;
    protected UserId actorId;
    protected UserName actorName;

    @Before
    public void init() {
        userIdCounter = 0;
        userNameCounter = 0;
        room = new RoomConfiguration().roomCreator().createRoom(new GameplayIdleEventHandler(), new GameRunningEndlesslyAfterStartCreator(), new UsersCountLimit(5));
        actorId = randomUserId();
        actorName = randomValidUserName();
    }

    protected <L, R> Either<L, R> failure(L failure) {
        return Either.left(failure);
    }

    protected <L, R> Either<L, R> success(R success) {
        return Either.right(success);
    }

    protected <L, R> boolean isSuccess(Either<L, R> either) {
        return either.isRight();
    }

    protected UserId randomUserId() {
        return new UserId("id-" + userIdCounter++);
    }

    protected UserName randomValidUserName() {
        return new UserName("name-" + userNameCounter++);
    }

    protected PlaygroundState lobbyState() {
        return room.getState().getPlaygroundState();
    }

    protected boolean gameIsRunning() {
        return lobbyState().isGameRunning();
    }

    protected GameOptions currentGameOptions() {
        return lobbyState().getGameOptions();
    }

    protected PlayerNumber freeSeatNumber() {
        return lobbyState()
                .getSeats().stream()
                .filter(seat -> !seat.isTaken())
                .map(Seat::getPlayerNumber)
                .findAny().get();
    }

    protected boolean adminIsChosen() {
        return lobbyState()
                .getSeats().stream()
                .anyMatch(Seat::isAdmin);
    }

    protected GameOptions newGameOptions() {
        return new GameOptions(newGridSize(), newGameSpeed(), newWalls());
    }

    private GridSize newGridSize() {
        return Stream.of(GridSize.values())
                .filter(gridSize -> !gridSize.equals(currentGameOptions().getGridSize()))
                .findAny()
                .get();
    }

    private GameSpeed newGameSpeed() {
        return Stream.of(GameSpeed.values())
                .filter(gameSpeed -> !gameSpeed.equals(currentGameOptions().getGameSpeed()))
                .findAny()
                .get();
    }

    private Walls newWalls() {
        return Stream.of(Walls.values())
                .filter(walls -> !walls.equals(currentGameOptions().getWalls()))
                .findAny()
                .get();
    }
}