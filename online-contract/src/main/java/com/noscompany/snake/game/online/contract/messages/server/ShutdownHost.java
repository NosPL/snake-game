package com.noscompany.snake.game.online.contract.messages.server;

import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.Value;

@Value
public class ShutdownHost {
    UserId hostId;
}