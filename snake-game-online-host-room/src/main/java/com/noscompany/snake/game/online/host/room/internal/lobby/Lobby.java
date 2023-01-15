package com.noscompany.snake.game.online.host.room.internal.lobby;


import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.gameplay.SnakeGameplay;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static io.vavr.control.Option.of;

@AllArgsConstructor
public class Lobby {
    private final Seats seats;
    private final GameCreator gameCreator;
    private GameOptions gameOptions;
    private SnakeGameplay snakeGameplay;

    public synchronized Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userName, PlayerNumber seatNumber) {
        if (snakeGameplay.isRunning())
            return left(FailedToTakeASeat.gameAlreadyRunning());
        return seats
                .takeOrChangeSeat(userName, seatNumber)
                .peek(userSuccessfullyTookASeat -> recreateGame())
                .map(this::playerTookASeat);
    }

    private PlayerTookASeat playerTookASeat(Seat.UserSuccessfullyTookASeat event) {
        return new PlayerTookASeat(event.getUserName(), event.getPlayerNumber(), getLobbyState());
    }

    public synchronized Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userName) {
        return seats
                .freeUpSeat(userName)
                .peek(this::killPlayer)
                .peek(userFreedUpASeat -> recreateGameIfItIsNotRunning())
                .map(this::playerFreedUpASeat);
    }

    private void killPlayer(Seat.UserFreedUpASeat event) {
        snakeGameplay.killSnake(event.getFreedUpSeatNumber());
    }

    private PlayerFreedUpASeat playerFreedUpASeat(Seat.UserFreedUpASeat event) {
        return new PlayerFreedUpASeat(event.getUserName(), event.getFreedUpSeatNumber(), getLobbyState());
    }

    public synchronized Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userName, GameOptions gameOptions) {
        if (!userTookASeat(userName))
            return left(FailedToChangeGameOptions.requesterDidNotTakeASeat());
        if (!userIsAdmin(userName))
            return left(FailedToChangeGameOptions.requesterIsNotAdmin());
        if (snakeGameplay.isRunning())
            return left(FailedToChangeGameOptions.gameIsAlreadyRunning());
        this.gameOptions = gameOptions;
        recreateGame();
        return right(new GameOptionsChanged(getLobbyState()));
    }

    public synchronized Option<FailedToStartGame> startGame(String userName) {
        if (!userTookASeat(userName))
            return of(FailedToStartGame.requesterDidNotTakeASeat());
        if (!userIsAdmin(userName))
            return of(FailedToStartGame.requesterIsNotAdmin());
        if (snakeGameplay.isRunning())
            return of(FailedToStartGame.gameIsAlreadyRunning());
        recreateGame();
        snakeGameplay.start();
        return Option.none();
    }

    public void changeSnakeDirection(String userName, Direction direction) {
        seats
                .getNumberFor(userName)
                .peek(playerNumber -> snakeGameplay.changeSnakeDirection(playerNumber, direction));
    }

    public void cancelGame(String userName) {
        if (seats.userIsAdmin(userName))
            snakeGameplay.cancel();
    }

    public void pauseGame(String userName) {
        if (seats.userIsAdmin(userName))
            snakeGameplay.pause();
    }

    public void resumeGame(String userName) {
        if (seats.userIsAdmin(userName))
            snakeGameplay.resume();
    }

    public LobbyState getLobbyState() {
        return new LobbyState(
                gameOptions,
                seats.toDto(),
                snakeGameplay.isRunning(),
                snakeGameplay.getGameState());
    }

    public boolean userTookASeat(String userName) {
        return seats.userIsSitting(userName);
    }

    public boolean userIsAdmin(String userName) {
        return seats.userIsAdmin(userName);
    }

    private void recreateGameIfItIsNotRunning() {
        if (!snakeGameplay.isRunning())
            recreateGame();
    }

    private void recreateGame() {
        snakeGameplay = gameCreator.createGame(seats.getPlayerNumbers(), gameOptions);
    }
}