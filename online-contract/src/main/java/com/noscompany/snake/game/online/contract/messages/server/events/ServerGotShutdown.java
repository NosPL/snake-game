package com.noscompany.snake.game.online.contract.messages.server.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.PublicClientMessage;
import lombok.Value;

import static com.noscompany.snake.game.online.contract.messages.OnlineMessage.MessageType.SERVER_GOT_SHUTDOWN;

@Value
public class ServerGotShutdown implements PublicClientMessage {
    OnlineMessage.MessageType messageType = SERVER_GOT_SHUTDOWN;
}