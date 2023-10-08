package com.noscompany.snake.game.online.contract.messages.server.commands;

import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class StartServer {
    ServerParams serverParams;
}