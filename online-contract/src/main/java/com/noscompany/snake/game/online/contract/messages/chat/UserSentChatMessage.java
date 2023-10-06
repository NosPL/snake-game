package com.noscompany.snake.game.online.contract.messages.chat;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class UserSentChatMessage implements OnlineMessage {
    MessageType messageType = MessageType.USER_SENT_CHAT_MESSAGE;
    UserId userId;
    String userName;
    String message;
}