package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.playground.SendPlaygroundStateToRemoteClient;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.host.room.internal.playground.Playground;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.Map;

import static io.vavr.control.Either.left;

@AllArgsConstructor
class RoomFacade implements Room {
    private final Map<UserId, UserName> userRegistry;
    private final Playground playground;

    @Override
    public void newUserEnteredRoom(UserId userId, UserName userName) {
        userRegistry.put(userId, userName);
    }

    @Override
    public Option<PlayerFreedUpASeat> userLeftRoom(UserId userId) {
        userRegistry.remove(userId);
        return playground
                .freeUpASeat(userId)
                .toOption();
    }

    @Override
    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(UserId userId, PlayerNumber playerNumber) {
        return findUserName(userId)
                .toEither(FailedToTakeASeat.userNotInTheRoom(userId))
                .flatMap(userName -> playground.takeASeat(userId, userName, playerNumber));
    }

    @Override
    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(UserId userId) {
        if (!userRegistry.containsKey(userId))
            return left(FailedToFreeUpSeat.userNotInTheRoom(userId));
        return playground.freeUpASeat(userId);
    }

    @Override
    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(UserId userId, GameOptions gameOptions) {
        if (!userRegistry.containsKey(userId))
            return left(FailedToChangeGameOptions.userNotInTheRoom(userId));
        return playground.changeGameOptions(userId, gameOptions);
    }

    @Override
    public Option<FailedToStartGame> startGame(UserId userId) {
        if (!userRegistry.containsKey(userId))
            return Option.of(FailedToStartGame.userIsNotInTheRoom(userId));
        return playground.startGame(userId);
    }

    @Override
    public Option<FailedToChangeSnakeDirection> changeSnakeDirection(UserId userId, Direction direction) {
        if (!userRegistry.containsKey(userId))
            return Option.of(FailedToChangeSnakeDirection.userNotInTheRoom(userId));
        return playground.changeSnakeDirection(userId, direction);
    }

    @Override
    public Option<FailedToCancelGame> cancelGame(UserId userId) {
        if (!userRegistry.containsKey(userId))
            return Option.of(FailedToCancelGame.userNotInTheRoom(userId));
        return playground.cancelGame(userId);
    }

    @Override
    public Option<FailedToPauseGame> pauseGame(UserId userId) {
        if (!userRegistry.containsKey(userId))
            return Option.of(FailedToPauseGame.userNotInTheRoom(userId));
        return playground.pauseGame(userId);
    }

    @Override
    public Option<FailedToResumeGame> resumeGame(UserId userId) {
        if (!userRegistry.containsKey(userId))
            return Option.of(FailedToResumeGame.userNotInTheRoom(userId));
        return playground.resumeGame(userId);
    }

    @Override
    public SendPlaygroundStateToRemoteClient newRemoteClientConnected(UserId remoteClientId) {
        return new SendPlaygroundStateToRemoteClient(remoteClientId, playground.getPlaygroundState());
    }

    @Override
    public PlaygroundState getPlaygroundState() {
        return playground.getPlaygroundState();
    }

    private Option<UserName> findUserName(UserId userId) {
        return Option.of(userRegistry.get(userId));
    }

    @Override
    public boolean userIsAdmin(UserId userId) {
        return playground.userIsAdmin(userId);
    }

    @Override
    public boolean userIsSitting(UserId userId) {
        return playground.userTookASeat(userId);
    }

    @Override
    public boolean containsUserWithId(UserId userId) {
        return userRegistry.containsKey(userId);
    }

    @Override
    public void terminate() {
        userRegistry.clear();
        playground.cancelGame();
    }
}