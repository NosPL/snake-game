package com.noscompany.snake.game.online.contract.messages.room;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToConnectToRoom implements OnlineMessage {
    MessageType messageType = MessageType.FAILED_TO_CONNECT_TO_THE_ROOM;
    Reason reason;

    public enum Reason {
        ROOM_IS_FULL
    }

    public static FailedToConnectToRoom roomIsFull() {
        return new FailedToConnectToRoom(Reason.ROOM_IS_FULL);
    }
}