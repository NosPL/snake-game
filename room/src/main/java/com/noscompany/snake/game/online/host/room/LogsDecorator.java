package com.noscompany.snake.game.online.host.room;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.room.*;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.host.room.dto.UserId;
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
        log.info("user with id {} tries to enter room with name {}", userId.getId(), userName.getName());
        return room
                .enter(userId, userName)
                .peek(success -> log.info("user with id {} entered room with name {}", userId.getId(), userName.getName()))
                .peekLeft(failure -> log.info("user with id {} failed to enter the room, reason: {}", userId.getId(), asString(failure.getReason())));
    }

    @Override
    public Either<FailedToTakeASeat, PlayerTookASeat> takeASeat(UserId userId, PlayerNumber playerNumber) {
        log.info("user with id {} tries to take a seat with number {}", userId.getId(), playerNumber);
        return room
                .takeASeat(userId, playerNumber)
                .peek(success -> log.info("user with id {} took a seat {}", userId.getId(), success.getPlayerNumber()))
                .peekLeft(failure -> log.info("user with id {} failed to take a seat, reason: {}", userId.getId(), asString(failure.getReason())));
    }

    @Override
    public Either<FailedToFreeUpSeat, PlayerFreedUpASeat> freeUpASeat(UserId userId) {
        log.info("user with id {} tries to free up a seat", userId.getId());
        return room
                .freeUpASeat(userId)
                .peek(success -> log.info("user with id {} freed up a seat", userId.getId()))
                .peekLeft(failure -> log.info("user with id {} failed to free up a seat, reason: {}", userId.getId(), asString(failure.getReason())));
    }

    @Override
    public Either<FailedToChangeGameOptions, GameOptionsChanged> changeGameOptions(UserId userId, GameOptions gameOptions) {
        log.info("user with id {} tries to change game options to {}", userId.getId(), gameOptions);
        return room
                .changeGameOptions(userId, gameOptions)
                .peek(success -> log.info("user with id {} changed game options to {}", userId.getId(), gameOptions))
                .peekLeft(failure -> log.info("user with id {} failed to change game options, reason: {}", userId.getId(), asString(failure.getReason())));
    }

    @Override
    public Option<FailedToStartGame> startGame(UserId userId) {
        log.info("user with id {} tries to start game", userId.getId());
        return room
                .startGame(userId)
                .peek(failure -> log.info("user with id {} failed start the game, reason: {}", userId.getId(), asString(failure.getReason())));
    }

    @Override
    public void changeSnakeDirection(UserId userId, Direction direction) {
        room.changeSnakeDirection(userId, direction);
    }

    @Override
    public void cancelGame(UserId userId) {
        room.cancelGame(userId);
    }

    @Override
    public void pauseGame(UserId userId) {
        room.pauseGame(userId);
    }

    @Override
    public void resumeGame(UserId userId) {
        room.resumeGame(userId);
    }

    @Override
    public Either<FailedToSendChatMessage, UserSentChatMessage> sendChatMessage(UserId userId, String messageContent) {
        log.info("user with id {} tries to send chat message: {}", userId.getId(), messageContent);
        return room
                .sendChatMessage(userId, messageContent)
                .peek(success -> log.info("user with id {} sent chat message: {}", userId.getId(), messageContent))
                .peekLeft(failure -> log.info("user with id {} failed to send chat message, reason: {}", userId.getId(), asString(failure.getReason())));
    }

    @Override
    public Option<UserLeftRoom> leave(UserId userId) {
        log.info("user with id {} tries to leave the room", userId.getId());
        return room
                .leave(userId)
                .peek(success -> logIfUserFreedUpSeat(userId, success.getPlayerFreedUpASeat()))
                .peek(success -> log.info("user with id {} and name {} left the room", userId.getId(), success.getUserName()));

    }

    private void logIfUserFreedUpSeat(UserId userId, Option<PlayerFreedUpASeat> playerFreedUpASeat) {
        playerFreedUpASeat
                .peek(event -> log.info("user with id {} freed up a seat with number {}", userId.getId(), event.getFreedUpPlayerNumber()));
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

    private String asString(Enum<?> enumm) {
        return enumm.toString().toLowerCase().replace('_', ' ');
    }
}