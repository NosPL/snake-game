package com.noscompany.snake.game.commons.messages.events.room;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToEnterRoom implements OnlineMessage {
    MessageType messageType = MessageType.FAILED_TO_ENTER_THE_ROOM;
    String userName;

    public static FailedToEnterRoom userNameAlreadyInUse(String userName) {
        return new FailedToEnterRoom(userName);
    }
}