package com.noscompany.snake.game.online.contract.messages.network;

import com.noscompany.snake.game.online.contract.messages.DedicatedClientMessage;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class YourIdGotInitialized implements DedicatedClientMessage {
    OnlineMessage.MessageType messageType = MessageType.YOUR_ID_GOT_INITIALIZED;
    UserId userId;

    @Override
    public MessageType getMessageType() {
        return null;
    }
}