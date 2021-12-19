package com.noscompany.snake.game.commons.messages.commands.room;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class EnterRoom implements OnlineMessage {
    MessageType messageType = MessageType.ENTER_THE_ROOM;
    String userName;
}