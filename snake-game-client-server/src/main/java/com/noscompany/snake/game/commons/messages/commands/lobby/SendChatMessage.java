package com.noscompany.snake.game.commons.messages.commands.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class SendChatMessage implements MessageDto {
    MessageType messageType = MessageType.SEND_CHAT_MESSAGE;
    String messageContent;
}