package com.noscompany.snake.game.online.contract.messages.chat;

import com.noscompany.snake.game.online.contract.messages.PublicClientMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class UserSentChatMessage implements PublicClientMessage {
    MessageType messageType = MessageType.USER_SENT_CHAT_MESSAGE;
    UserId userId;
    UserName userName;
    String message;
}