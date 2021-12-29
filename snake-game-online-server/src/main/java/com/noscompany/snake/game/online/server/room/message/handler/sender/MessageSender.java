package com.noscompany.snake.game.online.server.room.message.handler.sender;

import com.noscompany.snake.game.commons.OnlineMessage;
import org.atmosphere.cpr.AtmosphereResource;
import snake.game.core.SnakeGameEventHandler;
import snake.game.core.events.*;

public interface MessageSender extends SnakeGameEventHandler {

    void send(AtmosphereResource r, OnlineMessage onlineMessage);

    void sendToAll(OnlineMessage onlineMessage);
}
