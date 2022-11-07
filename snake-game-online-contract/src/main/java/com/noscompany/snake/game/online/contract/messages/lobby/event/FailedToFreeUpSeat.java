package com.noscompany.snake.game.online.contract.messages.lobby.event;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToFreeUpSeat implements OnlineMessage {
    MessageType messageType = MessageType.FAILED_TO_FREE_UP_A_SEAT;
    Reason reason;

    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        USER_DID_NOT_TAKE_A_SEAT
    }
    public static FailedToFreeUpSeat userNotInTheRoom() {
        return new FailedToFreeUpSeat(Reason.USER_NOT_IN_THE_ROOM);
    }

    public static FailedToFreeUpSeat userDidNotTakeASeat() {
        return new FailedToFreeUpSeat(Reason.USER_DID_NOT_TAKE_A_SEAT);
    }
}