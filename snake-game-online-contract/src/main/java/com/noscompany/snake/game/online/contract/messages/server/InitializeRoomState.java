package com.noscompany.snake.game.online.contract.messages.server;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class InitializeRoomState implements OnlineMessage {
    OnlineMessage.MessageType messageType = OnlineMessage.MessageType.USER_CONNECTED_TO_THE_SERVER;
    RoomState roomState;
}