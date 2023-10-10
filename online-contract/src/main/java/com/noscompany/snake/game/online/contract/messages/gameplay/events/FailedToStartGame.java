package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.DedicatedClientMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToStartGame implements DedicatedClientMessage {
    MessageType messageType = MessageType.FAILED_TO_START_GAME;
    UserId userId;
    Reason reason;

    public static FailedToStartGame requesterDidNotTakeASeat(UserId userId) {
        return new FailedToStartGame(userId, Reason.REQUESTER_DID_NOT_TAKE_A_SEAT);
    }

    public static FailedToStartGame gameIsAlreadyRunning(UserId userId) {
        return new FailedToStartGame(userId, Reason.GAME_IS_ALREADY_RUNNING);
    }

    public static FailedToStartGame requesterIsNotAdmin(UserId userId) {
        return new FailedToStartGame(userId, Reason.REQUESTER_IS_NOT_ADMIN);
    }

    public static FailedToStartGame userIsNotInTheRoom(UserId userId) {
        return new FailedToStartGame(userId, Reason.USER_IS_NOT_IN_THE_ROOM);
    }

    public enum Reason {
        REQUESTER_DID_NOT_TAKE_A_SEAT,
        GAME_IS_ALREADY_RUNNING,
        REQUESTER_IS_NOT_ADMIN,
        USER_IS_NOT_IN_THE_ROOM
    }
}