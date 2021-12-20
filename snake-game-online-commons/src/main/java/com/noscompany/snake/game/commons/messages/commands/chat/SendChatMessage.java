package com.noscompany.snake.game.commons.messages.commands.chat;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class SendChatMessage implements OnlineMessage {
    MessageType messageType = MessageType.SEND_CHAT_MESSAGE;
    String messageContent;
}
