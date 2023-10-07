package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.host.ports.RoomApiForHost;

public class SnakeOnlineHostConfiguration {

    public SnakeOnlineHost snakeOnlineHost(RoomApiForHost roomApiForHost, UserId hostId) {
        return new SnakeOnlineHostImpl(hostId, roomApiForHost);
    }
}