package com.noscompany.snake.game.online.contract.messages.server.events;

import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.Value;

@Value
public class RemoteClientDisconnected {
    UserId remoteClientId;
}