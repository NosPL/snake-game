package com.noscompany.snake.game.online.contract.messages.host;

import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.Value;

@Value
public class HostGotShutdown {
    UserId hostId;
}