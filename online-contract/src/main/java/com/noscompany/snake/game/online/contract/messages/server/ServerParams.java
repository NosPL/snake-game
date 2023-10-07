package com.noscompany.snake.game.online.contract.messages.server;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class ServerParams {
    String ipAddress;
    String port;
}