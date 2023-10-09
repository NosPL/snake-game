package com.noscompany.snake.game.online.host.dependency.configurator;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.chat.ChatConfiguration;
import com.noscompany.snake.game.online.contract.messages.user.registry.UsersCountLimit;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import com.noscompany.snake.game.online.playground.PlaygroundConfiguration;
import com.noscompany.snake.game.online.seats.SeatsConfiguration;
import com.noscompany.snake.game.online.websocket.WebsocketConfiguration;
import com.noscompany.snake.online.user.registry.UserRegistryConfiguration;
import snake.game.gameplay.GameplayConfiguration;

public class SnakeOnlineHostDependencyConfigurator {

    public void configureDependencies(UsersCountLimit usersCountLimit, MessagePublisher messagePublisher) {
        new UserRegistryConfiguration().create(usersCountLimit, messagePublisher);
        new SeatsConfiguration().create(messagePublisher);
        new ChatConfiguration().createChat(messagePublisher);
        var websocketCreator = new WebsocketConfiguration().websocketCreator();
        new ServerConfiguration().server(websocketCreator, messagePublisher);
        var gameplayCreator = new GameplayConfiguration().snakeGameplayCreator();
        new PlaygroundConfiguration().createPlayground(messagePublisher, gameplayCreator);
    }
}