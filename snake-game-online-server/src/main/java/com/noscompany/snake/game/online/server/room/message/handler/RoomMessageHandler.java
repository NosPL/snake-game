package com.noscompany.snake.game.online.server.room.message.handler;

import com.noscompany.snake.game.commons.messages.dto.RoomState;
import org.atmosphere.cpr.AtmosphereResource;

public interface RoomMessageHandler {

    void handle(AtmosphereResource atmosphereResource, String message);

    void removeUserById(String userId);

    RoomState getRoomState();
}