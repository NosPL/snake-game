package com.noscompany.snake.game.online.host.room.message.dispatcher.ports;

import com.noscompany.snake.game.online.host.room.message.dispatcher.RoomCommandHandlerForRemoteClients;
import io.vavr.control.Option;
import lombok.Value;

public interface Server {

    Option<StartError> start(int port, RoomCommandHandlerForRemoteClients handlerForRemoteClients);

    void shutdown();

    @Value
    class StartError {
        Throwable cause;
    }
}