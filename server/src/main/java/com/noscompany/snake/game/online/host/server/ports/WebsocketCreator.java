package com.noscompany.snake.game.online.host.server.ports;

import com.noscompany.snake.game.online.host.server.WebsocketEventHandler;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import io.vavr.control.Try;

public interface WebsocketCreator {
    Try<Websocket> create(ServerParams serverParams, WebsocketEventHandler websocketEventHandler);
}