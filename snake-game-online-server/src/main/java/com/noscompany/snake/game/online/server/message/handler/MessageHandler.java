package com.noscompany.snake.game.online.server.message.handler;

import org.atmosphere.cpr.AtmosphereResource;

public interface MessageHandler {

    void handle(AtmosphereResource atmosphereResource, String message);

    void userDisconnected(String connectionId);
}