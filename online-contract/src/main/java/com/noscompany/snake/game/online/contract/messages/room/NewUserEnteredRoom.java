package com.noscompany.snake.game.online.contract.messages.room;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class NewUserEnteredRoom implements OnlineMessage {
    MessageType messageType = MessageType.NEW_USER_ENTERED_ROOM;
    String userName;
    RoomState roomState;
}