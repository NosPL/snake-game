package com.noscompany.snake.game.online.host.room.internal.playground;


import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
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

    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(String userName, PlayerNumber seatNumber) {
        if (gameplay.isRunning())
            return left(FailedToTakeASeat.gameAlreadyRunning());
        return seats
                .takeOrChangeSeat(userName, seatNumber)
                .peek(userSuccessfullyTookASeat -> recreateGame())
                .map(this::playerTookASeat);
    }

    private PlayerTookASeat playerTookASeat(Seat.UserSuccessfullyTookASeat event) {
        return new PlayerTookASeat(event.getUserName(), event.getPlayerNumber(), getPlaygroundState());
    }

    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(String userName) {
        return seats
                .freeUpSeat(userName)
                .peek(this::killPlayer)
                .peek(userFreedUpASeat -> recreateGameIfItIsNotRunning())
                .map(this::playerFreedUpASeat);
    }

    private void killPlayer(Seat.UserFreedUpASeat event) {
        gameplay.killSnake(event.getFreedUpSeatNumber());
    }

    private PlayerFreedUpASeat playerFreedUpASeat(Seat.UserFreedUpASeat event) {
        return new PlayerFreedUpASeat(event.getUserName(), event.getFreedUpSeatNumber(), getPlaygroundState());
    }

    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(String userName, GameOptions gameOptions) {
        if (!userTookASeat(userName))
            return left(FailedToChangeGameOptions.requesterDidNotTakeASeat());
        if (!userIsAdmin(userName))
            return left(FailedToChangeGameOptions.requesterIsNotAdmin());
        if (gameplay.isRunning())
            return left(FailedToChangeGameOptions.gameIsAlreadyRunning());
        this.gameOptions = gameOptions;
        recreateGame();
        return right(new GameOptionsChanged(getPlaygroundState()));
    }

    public Option<FailedToStartGame> startGame(String userName) {
        if (!userTookASeat(userName))
            return of(FailedToStartGame.requesterDidNotTakeASeat());
        if (!userIsAdmin(userName))
            return of(FailedToStartGame.requesterIsNotAdmin());
        if (gameplay.isRunning())
            return of(FailedToStartGame.gameIsAlreadyRunning());
        recreateGame();
        gameplay.start();
        return Option.none();
    }

    public void changeSnakeDirection(String userName, Direction direction) {
        seats
                .getNumberFor(userName)
                .peek(playerNumber -> gameplay.changeSnakeDirection(playerNumber, direction));
    }

    public void cancelGame(String userName) {
        if (seats.userIsAdmin(userName))
            gameplay.cancel();
    }

    public void pauseGame(String userName) {
        if (seats.userIsAdmin(userName))
            gameplay.pause();
    }

    public void resumeGame(String userName) {
        if (seats.userIsAdmin(userName))
            gameplay.resume();
    }

    public PlaygroundState getPlaygroundState() {
        return new PlaygroundState(
                gameOptions,
                seats.toDto(),
                gameplay.isRunning(),
                gameplay.getGameState());
    }

    public boolean userTookASeat(String userName) {
        return seats.userIsSitting(userName);
    }

    public boolean userIsAdmin(String userName) {
        return seats.userIsAdmin(userName);
    }

    private void recreateGameIfItIsNotRunning() {
        if (!gameplay.isRunning())
            recreateGame();
    }

    private void recreateGame() {
        gameplay = gameCreatorAdapter.createGame(seats.getPlayerNumbers(), gameOptions);
    }
}