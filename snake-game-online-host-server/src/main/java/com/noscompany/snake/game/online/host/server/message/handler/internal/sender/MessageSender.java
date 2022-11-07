package com.noscompany.snake.game.online.host.server.message.handler.internal.sender;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import org.atmosphere.cpr.AtmosphereResource;
import snake.game.gameplay.SnakeGameEventHandler;

public interface MessageSender extends SnakeGameEventHandler {

    void send(AtmosphereResource r, OnlineMessage onlineMessage);

    void sendToAll(OnlineMessage onlineMessage);
}