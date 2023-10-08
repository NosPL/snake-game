package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.room.*;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class LogsDecorator implements Room {
    private final Room room;

    @Override
    public Either<FailedToEnterRoom, NewUserEnteredRoom> enter(UserId userId, UserName userName) {
        return room
                .enter(userId, userName)
                .peek(success -> log.info("user with id {} entered room with name {}", userId.getId(), userName.getName()))
                .peekLeft(failure -> log.info("user with id {} failed to enter the room, reason: {}", userId.getId(), asString(failure.getReason())));
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
    public Option<UserLeftRoom> leave(UserId userId) {
        return room
                .leave(userId)
                .peek(success -> logIfUserFreedUpSeat(userId, success.getPlayerFreedUpASeat()))
                .peek(success -> log.info("user with id {} and name {} left the room", userId.getId(), success.getUserName()))
                .onEmpty(() -> log.info("user with id {} failed to leave the room because he is not in it", userId.getId()));

    }

    private void logIfUserFreedUpSeat(UserId userId, Option<PlayerFreedUpASeat> playerFreedUpASeat) {
        playerFreedUpASeat
                .peek(event -> log.info("user with id {} and name {} freed up a seat with number {}", userId.getId(), event.getUserName(), event.getFreedUpPlayerNumber()));
    }

    @Override
    public RoomState getState() {
        return room.getState();
    }

    @Override
    public boolean hasUserWithId(UserId userId) {
        return room.hasUserWithId(userId);
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
    public boolean isFull() {
        return room.isFull();
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