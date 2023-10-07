package com.noscompany.snake.game.online.host.server.ports;

import com.noscompany.snake.game.online.contract.messages.server.FailedToStartServer;
import com.noscompany.snake.game.online.host.server.WebsocketEventHandler;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import io.vavr.control.Either;
import io.vavr.control.Try;

public interface WebsocketCreator {
    Either<FailedToStartServer, Websocket> create(ServerParams serverParams, WebsocketEventHandler websocketEventHandler);
}