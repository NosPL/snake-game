package com.noscompany.snake.game.online.host.dependency.configurator;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snake.game.online.host.mediator.MediatorConfiguration;
import com.noscompany.snake.game.online.host.room.RoomConfiguration;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import com.noscompany.snake.game.online.websocket.WebsocketConfiguration;
import snake.game.gameplay.GameplayConfiguration;

public class SnakeOnlineHostDependencyConfigurator {

    public void snakeOnlineHost(UsersCountLimit usersCountLimit, MessagePublisher messagePublisher) {
        var websocketCreator = new WebsocketConfiguration().websocketCreator();
        var server = new ServerConfiguration().server(websocketCreator);
        var roomCreator = new RoomConfiguration().roomCreator();
        var snakeGameplayCreator = new GameplayConfiguration().snakeGameplayCreator();
        new MediatorConfiguration().roomApiForRemoteClients(messagePublisher, server, roomCreator, usersCountLimit, snakeGameplayCreator);
    }
}