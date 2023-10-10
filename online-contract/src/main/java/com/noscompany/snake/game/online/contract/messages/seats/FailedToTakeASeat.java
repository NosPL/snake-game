package com.noscompany.snake.game.online.contract.messages.seats;

import com.noscompany.snake.game.online.contract.messages.DedicatedClientMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToTakeASeat implements DedicatedClientMessage {
    MessageType messageType = MessageType.FAILED_TO_TAKE_A_SEAT;
    UserId userId;
    Reason reason;

    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        GAME_ALREADY_RUNNING,
        SEAT_ALREADY_TAKEN
    }

    public static FailedToTakeASeat userNotInTheRoom(UserId userId) {
        return new FailedToTakeASeat(userId, Reason.USER_NOT_IN_THE_ROOM);
    }

    public static FailedToTakeASeat gameAlreadyRunning(UserId userId) {
        return new FailedToTakeASeat(userId, Reason.GAME_ALREADY_RUNNING);
    }

    public static FailedToTakeASeat seatAlreadyTaken(UserId userId) {
        return new FailedToTakeASeat(userId, Reason.SEAT_ALREADY_TAKEN);
    }
}