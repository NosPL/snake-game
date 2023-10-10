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
public class FailedToFreeUpSeat implements DedicatedClientMessage {
    MessageType messageType = MessageType.FAILED_TO_FREE_UP_A_SEAT;
    UserId userId;
    Reason reason;

    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        USER_DID_NOT_TAKE_A_SEAT
    }
    public static FailedToFreeUpSeat userNotInTheRoom(UserId userId) {
        return new FailedToFreeUpSeat(userId, Reason.USER_NOT_IN_THE_ROOM);
    }

    public static FailedToFreeUpSeat userDidNotTakeASeat(UserId userId) {
        return new FailedToFreeUpSeat(userId, Reason.USER_DID_NOT_TAKE_A_SEAT);
    }
}