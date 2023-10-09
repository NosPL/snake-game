package com.noscompany.snake.game.online.contract.messages.server.events;

import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import lombok.Value;

@Value
public class ServerStarted {
    ServerParams serverParams;
}