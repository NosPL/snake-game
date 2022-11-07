package com.noscompany.snake.game.online.host.server.message.handler;

import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import org.atmosphere.cpr.AtmosphereResource;

public interface RoomMessageHandler {

    void newUserConnected(AtmosphereResource atmosphereResource);

    void handle(AtmosphereResource atmosphereResource, String message);

    void removeUserById(String userId);

    RoomState getRoomState();
}