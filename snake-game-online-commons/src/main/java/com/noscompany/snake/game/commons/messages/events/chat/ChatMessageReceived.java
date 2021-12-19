package com.noscompany.snake.game.commons.messages.events.chat;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class ChatMessageReceived implements OnlineMessage {
    MessageType messageType = MessageType.CHAT_MESSAGE_RECEIVED;
    String userName;
    String message;
}