package com.noscompany.snake.game.online.playground;


import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.gameplay.Gameplay;

import java.util.Map;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static io.vavr.control.Option.of;

@AllArgsConstructor
public class Playground {
    private final Map<UserId, UserName> userRegistry;
    private final Seats seats;
    private final GameCreatorAdapter gameCreatorAdapter;
    private GameOptions gameOptions;
    private Gameplay gameplay;

    public void newUserEnteredRoom(UserId userId, UserName userName) {
        userRegistry.put(userId, userName);
    }

    public Option<PlayerFreedUpASeat> userLeftRoom(UserId userId) {
        return freeUpASeat(userId)
                .peekLeft(success -> userRegistry.remove(userId))
                .toOption();
    }

    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(UserId userId, PlayerNumber seatNumber) {
        if (gameplay.isRunning())
            return left(FailedToTakeASeat.gameAlreadyRunning(userId));
        if (!userRegistry.containsKey(userId))
            return left(FailedToTakeASeat.userNotInTheRoom(userId));
        return findUserName(userId)
                .toEither(FailedToTakeASeat.userNotInTheRoom(userId))
                .flatMap(userName -> seats.takeOrChangeSeat(userId, userName, seatNumber))
                .peek(userSuccessfullyTookASeat -> recreateGame())
                .map(event -> playerTookASeat(userId, event));
    }

    private PlayerTookASeat playerTookASeat(UserId userId, Seat.UserSuccessfullyTookASeat event) {
        return new PlayerTookASeat(userId, event.getUserName().getName(), event.getPlayerNumber(), getPlaygroundState());
    }

    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(UserId userId) {
        if (!userRegistry.containsKey(userId))
            return left(FailedToFreeUpSeat.userNotInTheRoom(userId));
        return seats
                .freeUpSeat(userId)
                .peek(this::killPlayer)
                .peek(userFreedUpASeat -> recreateGameIfItIsNotRunning())
                .map(event -> playerFreedUpASeat(userId, event));
    }

    private void killPlayer(Seat.UserFreedUpASeat event) {
        gameplay.killSnake(event.getFreedUpSeatNumber());
    }

    private PlayerFreedUpASeat playerFreedUpASeat(UserId userId, Seat.UserFreedUpASeat event) {
        return new PlayerFreedUpASeat(userId, event.getUserName().getName(), event.getFreedUpSeatNumber(), getPlaygroundState());
    }

    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(UserId userId, GameOptions gameOptions) {
        if (!userRegistry.containsKey(userId))
            return left(FailedToChangeGameOptions.userNotInTheRoom(userId));
        if (!userTookASeat(userId))
            return left(FailedToChangeGameOptions.requesterDidNotTakeASeat(userId));
        if (!userIsAdmin(userId))
            return left(FailedToChangeGameOptions.requesterIsNotAdmin(userId));
        if (gameplay.isRunning())
            return left(FailedToChangeGameOptions.gameIsAlreadyRunning(userId));
        this.gameOptions = gameOptions;
        recreateGame();
        return right(new GameOptionsChanged(getPlaygroundState()));
    }

    public Option<FailedToStartGame> startGame(UserId userId) {
        if (!userRegistry.containsKey(userId))
            return Option.of(FailedToStartGame.userIsNotInTheRoom(userId));
        if (!userTookASeat(userId))
            return of(FailedToStartGame.requesterDidNotTakeASeat(userId));
        if (!userIsAdmin(userId))
            return of(FailedToStartGame.requesterIsNotAdmin(userId));
        if (gameplay.isRunning())
            return of(FailedToStartGame.gameIsAlreadyRunning(userId));
        recreateGame();
        gameplay.start();
        return Option.none();
    }

    public Option<FailedToChangeSnakeDirection> changeSnakeDirection(UserId userId, Direction direction) {
        if (!userRegistry.containsKey(userId))
            return Option.of(FailedToChangeSnakeDirection.userNotInTheRoom(userId));
        if (!seats.userIsSitting(userId))
            return Option.of(FailedToChangeSnakeDirection.playerDidNotTakeASeat(userId));
        if (!gameplay.isRunning())
            return Option.of(FailedToChangeSnakeDirection.gameNotStarted(userId));
        return seats
                .getNumberFor(userId)
                .peek(playerNumber -> gameplay.changeSnakeDirection(playerNumber, direction))
                .toEither(FailedToChangeSnakeDirection.playerDidNotTakeASeat(userId))
                .swap().toOption();
    }

    public Option<FailedToCancelGame> cancelGame(UserId userId) {
        if (!userRegistry.containsKey(userId))
            return Option.of(FailedToCancelGame.userNotInTheRoom(userId));
        if (!seats.userIsSitting(userId))
            return Option.of(FailedToCancelGame.playerDidNotTakeASeat(userId));
        if (!seats.userIsAdmin(userId))
            return Option.of(FailedToCancelGame.playerIsNotAdmin(userId));
        if (!gameplay.isRunning())
            return Option.of(FailedToCancelGame.gameNotStarted(userId));
        gameplay.cancel();
        return Option.none();
    }

    public Option<FailedToPauseGame> pauseGame(UserId userId) {
        if (!userRegistry.containsKey(userId))
            return Option.of(FailedToPauseGame.userNotInTheRoom(userId));
        if (!seats.userIsSitting(userId))
            return Option.of(FailedToPauseGame.playerDidNotTakeASeat(userId));
        if (!seats.userIsAdmin(userId))
            return Option.of(FailedToPauseGame.playerIsNotAdmin(userId));
        if (!gameplay.isRunning())
            return Option.of(FailedToPauseGame.gameNotStarted(userId));
        gameplay.pause();
        return Option.none();
    }

    public Option<FailedToResumeGame> resumeGame(UserId userId) {
        if (!userRegistry.containsKey(userId))
            return Option.of(FailedToResumeGame.userNotInTheRoom(userId));
        if (!seats.userIsSitting(userId))
            return Option.of(FailedToResumeGame.playerDidNotTakeASeat(userId));
        if (!seats.userIsAdmin(userId))
            return Option.of(FailedToResumeGame.playerIsNotAdmin(userId));
        if (!gameplay.isRunning())
            return Option.of(FailedToResumeGame.gameNotStarted(userId));
        gameplay.resume();
        return Option.none();
    }

    public PlaygroundState getPlaygroundState() {
        return new PlaygroundState(
                gameOptions,
                seats.toDto(),
                gameplay.isRunning(),
                gameplay.getGameState());
    }

    boolean userTookASeat(UserId userId) {
        return seats.userIsSitting(userId);
    }

    public boolean userIsAdmin(UserId userId) {
        return seats.userIsAdmin(userId);
    }

    void handle(HostGotShutdown event) {
        userRegistry.clear();
        gameplay.cancel();
    }

    private void recreateGameIfItIsNotRunning() {
        if (!gameplay.isRunning())
            recreateGame();
    }

    private void recreateGame() {
        gameplay = gameCreatorAdapter.createGame(seats.getTakenSeatsNumbers(), gameOptions);
    }

    private Option<UserName> findUserName(UserId userId) {
        return Option.of(userRegistry.get(userId));
    }

    public boolean containsUserWithId(UserId userId) {
        return userRegistry.containsKey(userId);
    }

    public boolean userIsSitting(UserId userId) {
        return seats.userIsSitting(userId);
    }

    public void terminate() {
        userRegistry.clear();
        gameplay.cancel();
    }
}