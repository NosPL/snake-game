package com.noscompany.snake.game.test.client;

import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.snake.game.online.chat.ChatConfiguration;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientConfiguration;
import com.noscompany.snake.game.online.contract.messages.user.registry.UsersCountLimit;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import com.noscompany.snake.game.online.playground.PlaygroundConfiguration;
import com.noscompany.snake.game.online.websocket.WebsocketConfiguration;
import com.noscompany.snake.online.user.registry.UserRegistryConfiguration;
import snake.game.gameplay.GameplayConfiguration;

public class SnakeOnlineTestClientConfiguration {

    public SnakeOnlineClient snakeOnlineTestClient(ClientEventHandler clientEventHandler) {
        var snakeOnlineClient = new SnakeOnlineClientConfiguration().create(clientEventHandler);
        var websocketCreator = new WebsocketConfiguration().websocketCreator();
        var messagePublisher = new MessagePublisherCreator().create();
        var server = new ServerConfiguration().server(websocketCreator, messagePublisher);
        new UserRegistryConfiguration().create(new UsersCountLimit(10), messagePublisher);
        new ChatConfiguration().createChat(messagePublisher);
        var gameplayCreator = new GameplayConfiguration().snakeGameplayCreator();
        new PlaygroundConfiguration().createPlayground(messagePublisher, gameplayCreator);
        return new SnakeOnlineTestClient(snakeOnlineClient, clientEventHandler, server);
    }
}