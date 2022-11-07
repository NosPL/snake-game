package com.noscompany.snake.game.online.host.room.internal.lobby;


import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import com.noscompany.snake.game.online.contract.messages.lobby.event.*;
import com.noscompany.snake.game.online.host.room.internal.lobby.internal.seats.Seats;
import com.noscompany.snake.game.online.host.room.internal.lobby.internal.seats.UserFreedUpASeat;
import com.noscompany.snake.game.online.host.room.internal.lobby.internal.seats.UserSuccessfullyTookASeat;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.gameplay.SnakeGame;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static io.vavr.control.Option.of;

@AllArgsConstructor
class LobbyImpl implements Lobby {
    private final Seats seats;
    private final GameCreator gameCreator;
    private GameOptions gameOptions;
    private SnakeGame snakeGame;

    @Override
    public synchronized Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userName, PlayerNumber seatNumber) {
        if (snakeGame.isRunning())
            return left(FailedToTakeASeat.gameAlreadyRunning());
        return seats
                .takeOrChangeSeat(userName, seatNumber)
                .peek(userSuccessfullyTookASeat -> recreateGame())
                .map(this::playerTookASeat);
    }

    private PlayerTookASeat playerTookASeat(UserSuccessfullyTookASeat event) {
        return new PlayerTookASeat(event.getUserName(), event.getPlayerNumber(), getLobbyState());
    }

    @Override
    public synchronized Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userName) {
        return seats
                .freeUpSeat(userName)
                .peek(this::killPlayer)
                .peek(userFreedUpASeat -> recreateGameIfItIsNotRunning())
                .map(this::playerFreedUpASeat);
    }

    private void killPlayer(UserFreedUpASeat event) {
        snakeGame.killSnake(event.getFreedUpSeatNumber());
    }

    private PlayerFreedUpASeat playerFreedUpASeat(UserFreedUpASeat event) {
        return new PlayerFreedUpASeat(event.getUserName(), event.getFreedUpSeatNumber(), getLobbyState());
    }

    @Override
    public synchronized Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userName,
                                                                                                GameOptions gameOptions) {
        if (!userIsSitting(userName))
            return left(FailedToChangeGameOptions.requesterDidNotTakeASeat());
        if (!userIsAdmin(userName))
            return left(FailedToChangeGameOptions.requesterIsNotAdmin());
        if (snakeGame.isRunning())
            return left(FailedToChangeGameOptions.gameIsAlreadyRunning());
        this.gameOptions = gameOptions;
        recreateGame();
        return right(new GameOptionsChanged(getLobbyState()));
    }

    @Override
    public synchronized Option<FailedToStartGame> startGame(String userName) {
        if (!userIsSitting(userName))
            return of(FailedToStartGame.requesterDidNotTakeASeat());
        if (!userIsAdmin(userName))
            return of(FailedToStartGame.requesterIsNotAdmin());
        if (snakeGame.isRunning())
            return of(FailedToStartGame.gameIsAlreadyRunning());
        recreateGame();
        snakeGame.start();
        return Option.none();
    }

    @Override
    public void changeSnakeDirection(String userName, Direction direction) {
        seats
                .getNumberFor(userName)
                .peek(playerNumber -> snakeGame.changeSnakeDirection(playerNumber, direction));
    }

    @Override
    public void cancelGame(String userName) {
        if (seats.userIsAdmin(userName))
            snakeGame.cancel();
    }

    @Override
    public void pauseGame(String userName) {
        if (seats.userIsAdmin(userName))
            snakeGame.pause();
    }

    @Override
    public void resumeGame(String userName) {
        if (seats.userIsAdmin(userName))
            snakeGame.resume();
    }

    @Override
    public LobbyState getLobbyState() {
        return new LobbyState(
                gameOptions,
                seats.toDto(),
                snakeGame.isRunning(),
                snakeGame.getGameState());
    }

    @Override
    public boolean userIsSitting(String userName) {
        return seats.userIsSitting(userName);
    }

    @Override
    public boolean userIsAdmin(String userName) {
        return seats.userIsAdmin(userName);
    }

    private void recreateGameIfItIsNotRunning() {
        if (!snakeGame.isRunning())
            recreateGame();
    }

    private void recreateGame() {
        snakeGame = gameCreator.createGame(seats.getPlayerNumbers(), gameOptions);
    }
}