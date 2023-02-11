package com.noscompany.snake.game.online.contract.messages.server;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.NoArgsConstructor;
import lombok.Value;

import static com.noscompany.snake.game.online.contract.messages.OnlineMessage.MessageType.SERVER_GOT_SHUTDOWN;

@Value
@NoArgsConstructor
public class ServerGotShutdown implements OnlineMessage {
    OnlineMessage.MessageType messageType = SERVER_GOT_SHUTDOWN;
}