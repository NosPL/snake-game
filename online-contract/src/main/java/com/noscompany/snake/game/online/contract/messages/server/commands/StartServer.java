package com.noscompany.snake.game.online.contract.messages.server.commands;

import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import lombok.Value;

@Value
public class StartServer {
    ServerParams serverParams;
}