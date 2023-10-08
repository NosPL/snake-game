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
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class LogsDecorator implements Room {
    private final Room room;

    @Override
    public void newUserEnteredRoom(UserId userId, UserName userName) {
        log.info("new user with id {} and name {} entered room", userId.getId(), userName.getName());
        room.newUserEnteredRoom(userId, userName);
    }

    @Override
    public Option<PlayerFreedUpASeat> userLeftRoom(UserId userId) {
        log.info("user with id {} left the room room", userId.getId());
        return room
                .userLeftRoom(userId)
                .peek(event -> log.info("user with id {} freed up a seat with number {}", event.getUserId(), event.getFreedUpPlayerNumber()));
    }

    @Override
    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(UserId userId, PlayerNumber playerNumber) {
        return room
                .takeASeat(userId, playerNumber)
                .peek(success -> log.info("user with id {} took a seat {}", userId.getId(), success.getPlayerNumber()))
                .peekLeft(failure -> log.info("user with id {} failed to take a seat, reason: {}", userId.getId(), asString(failure.getReason())));
    }

    @Override
    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(UserId userId) {
        return room
                .freeUpASeat(userId)
                .peek(success -> log.info("user with id {} freed up a seat", userId.getId()))
                .peekLeft(failure -> log.info("user with id {} failed to free up a seat, reason: {}", userId.getId(), asString(failure.getReason())));
    }

    @Override
    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(UserId userId, GameOptions gameOptions) {
        return room
                .changeGameOptions(userId, gameOptions)
                .peek(success -> log.info("user with id {} changed game options to {}", userId.getId(), gameOptions))
                .peekLeft(failure -> log.info("user with id {} failed to change game options, reason: {}", userId.getId(), asString(failure.getReason())));
    }

    @Override
    public Option<FailedToStartGame> startGame(UserId userId) {
        return room
                .startGame(userId)
                .peek(failure -> log.info("user with id {} failed start the game, reason: {}", userId.getId(), asString(failure.getReason())))
                .onEmpty(() -> log.info("start game command from user with id {} got accepted", userId.getId()));
    }

    @Override
    public Option<FailedToChangeSnakeDirection> changeSnakeDirection(UserId userId, Direction direction) {
        return room
                .changeSnakeDirection(userId, direction)
                .peek(failure -> log.info("user with id {} failed to change snake direction, reason: {}", userId.getId(), asString(failure.getReason())))
                .onEmpty(() -> log.info("change snake direction command from user with id {} and direction {} got accepted", userId.getId(), direction));
    }

    @Override
    public Option<FailedToCancelGame> cancelGame(UserId userId) {
        return room
                .cancelGame(userId)
                .peek(failure -> log.info("user with id {} failed to cancel game, reason: {}",userId.getId(), asString(failure.getReason())))
                .onEmpty(() -> log.info("cancel game command from user with id {} got accepted", userId.getId()));
    }

    @Override
    public Option<FailedToPauseGame> pauseGame(UserId userId) {
        return room
                .pauseGame(userId)
                .peek(failure -> log.info("user with id {} failed to pause game, reason: {}", userId.getId(), asString(failure.getReason())))
                .onEmpty(() -> log.info("pause game command from user with id {} got accepted", userId.getId()));
    }

    @Override
    public Option<FailedToResumeGame> resumeGame(UserId userId) {
        return room
                .resumeGame(userId)
                .peek(failure -> log.info("user with id {} failed to resume game, reason: {}", userId.getId(), asString(failure.getReason())))
                .onEmpty(() -> log.info("resume game command from user with id {} got accepted", userId.getId()));
    }

    @Override
    public SendPlaygroundStateToRemoteClient newRemoteClientConnected(UserId remoteClientId) {
        log.info("Sending playground state to remote client with id {}", remoteClientId.getId());
        return room.newRemoteClientConnected(remoteClientId);
    }

    @Override
    public PlaygroundState getPlaygroundState() {
        return room.getPlaygroundState();
    }

    @Override
    public boolean containsUserWithId(UserId userId) {
        return room.containsUserWithId(userId);
    }

    @Override
    public boolean userIsAdmin(UserId userId) {
        return room.userIsAdmin(userId);
    }

    @Override
    public boolean userIsSitting(UserId userId) {
        return room.userIsSitting(userId);
    }

    @Override
    public void terminate() {
        room.terminate();
        log.info("Room got terminated");
    }

    private String asString(Enum<?> enumm) {
        return enumm.toString().toLowerCase().replace('_', ' ');
    }
}