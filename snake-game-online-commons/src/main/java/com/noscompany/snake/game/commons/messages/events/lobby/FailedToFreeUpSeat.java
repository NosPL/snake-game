package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.OnlineMessage;
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
        USER_NOT_IN_THE_LOBBY
    }
    public static FailedToFreeUpSeat userNotInTheRoom() {
        return new FailedToFreeUpSeat(Reason.USER_NOT_IN_THE_ROOM);
    }
    public static FailedToFreeUpSeat userNotInTheLobby() {
        return new FailedToFreeUpSeat(Reason.USER_NOT_IN_THE_LOBBY);
    }
}
