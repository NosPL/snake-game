package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class ChatMessageReceived implements MessageDto {
    MessageType messageType = MessageType.CHAT_MESSAGE_RECEIVED;
    String chatMessageAuthor;
    String chatMessageContent;
}