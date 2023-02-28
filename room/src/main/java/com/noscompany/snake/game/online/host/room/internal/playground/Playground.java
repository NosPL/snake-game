package com.noscompany.snake.game.online.host.room.internal.playground;


import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.host.room.dto.UserId;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.gameplay.Gameplay;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static io.vavr.control.Option.of;

@AllArgsConstructor
public class Playground {
    private final Seats seats;
    private final GameCreatorAdapter gameCreatorAdapter;
    private GameOptions gameOptions;
    private Gameplay gameplay;

    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(UserId userId, UserName userName, PlayerNumber seatNumber) {
        if (gameplay.isRunning())
            return left(FailedToTakeASeat.gameAlreadyRunning());
        return seats
                .takeOrChangeSeat(userId, userName, seatNumber)
                .peek(userSuccessfullyTookASeat -> recreateGame())
                .map(this::playerTookASeat);
    }

    private PlayerTookASeat playerTookASeat(Seat.UserSuccessfullyTookASeat event) {
        return new PlayerTookASeat(event.getUserName().getName(), event.getPlayerNumber(), getPlaygroundState());
    }

    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(UserId userId) {
        return seats
                .freeUpSeat(userId)
                .peek(this::killPlayer)
                .peek(userFreedUpASeat -> recreateGameIfItIsNotRunning())
                .map(this::playerFreedUpASeat);
    }

    private void killPlayer(Seat.UserFreedUpASeat event) {
        gameplay.killSnake(event.getFreedUpSeatNumber());
    }

    private PlayerFreedUpASeat playerFreedUpASeat(Seat.UserFreedUpASeat event) {
        return new PlayerFreedUpASeat(event.getUserName().getName(), event.getFreedUpSeatNumber(), getPlaygroundState());
    }

    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(UserId userId, GameOptions gameOptions) {
        if (!userTookASeat(userId))
            return left(FailedToChangeGameOptions.requesterDidNotTakeASeat());
        if (!userIsAdmin(userId))
            return left(FailedToChangeGameOptions.requesterIsNotAdmin());
        if (gameplay.isRunning())
            return left(FailedToChangeGameOptions.gameIsAlreadyRunning());
        this.gameOptions = gameOptions;
        recreateGame();
        return right(new GameOptionsChanged(getPlaygroundState()));
    }

    public Option<FailedToStartGame> startGame(UserId userId) {
        if (!userTookASeat(userId))
            return of(FailedToStartGame.requesterDidNotTakeASeat());
        if (!userIsAdmin(userId))
            return of(FailedToStartGame.requesterIsNotAdmin());
        if (gameplay.isRunning())
            return of(FailedToStartGame.gameIsAlreadyRunning());
        recreateGame();
        gameplay.start();
        return Option.none();
    }

    public void changeSnakeDirection(UserId userId, Direction direction) {
        seats
                .getNumberFor(userId)
                .peek(playerNumber -> gameplay.changeSnakeDirection(playerNumber, direction));
    }

    public void cancelGame(UserId userId) {
        if (seats.userIsAdmin(userId))
            gameplay.cancel();
    }

    public void pauseGame(UserId userId) {
        if (seats.userIsAdmin(userId))
            gameplay.pause();
    }

    public void resumeGame(UserId userId) {
        if (seats.userIsAdmin(userId))
            gameplay.resume();
    }

    public PlaygroundState getPlaygroundState() {
        return new PlaygroundState(
                gameOptions,
                seats.toDto(),
                gameplay.isRunning(),
                gameplay.getGameState());
    }

    public boolean userTookASeat(UserId userId) {
        return seats.userIsSitting(userId);
    }

    public boolean userIsAdmin(UserId userId) {
        return seats.userIsAdmin(userId);
    }

    private void recreateGameIfItIsNotRunning() {
        if (!gameplay.isRunning())
            recreateGame();
    }

    private void recreateGame() {
        gameplay = gameCreatorAdapter.createGame(seats.getPlayerNumbers(), gameOptions);
    }
}