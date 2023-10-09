package com.noscompany.snake.game.online.contract.messages.user.registry;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class EnterRoom implements OnlineMessage {
    MessageType messageType = MessageType.ENTER_THE_ROOM;
    UserId userId;
    UserName userName;
}