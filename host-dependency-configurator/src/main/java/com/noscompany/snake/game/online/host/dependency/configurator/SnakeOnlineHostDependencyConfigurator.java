package com.noscompany.snake.game.online.host.dependency.configurator;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.chat.ChatConfiguration;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snake.game.online.host.room.RoomConfiguration;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import com.noscompany.snake.game.online.websocket.WebsocketConfiguration;
import snake.game.gameplay.GameplayConfiguration;

public class SnakeOnlineHostDependencyConfigurator {

    public void configureDependencies(UsersCountLimit usersCountLimit, MessagePublisher messagePublisher) {
        new ChatConfiguration().createChat(messagePublisher);
        var websocketCreator = new WebsocketConfiguration().websocketCreator();
        new ServerConfiguration().server(websocketCreator, messagePublisher);
        var gameplayCreator = new GameplayConfiguration().snakeGameplayCreator();
        new RoomConfiguration().roomCreator().createRoom(gameplayCreator, usersCountLimit, messagePublisher);
    }
}