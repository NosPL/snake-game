package com.noscompany.snake.game.test.client;

import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientConfiguration;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snake.game.online.host.room.RoomConfiguration;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import com.noscompany.snake.game.online.websocket.WebsocketConfiguration;
import snake.game.gameplay.GameplayConfiguration;

public class SnakeOnlineTestClientConfiguration {

    public SnakeOnlineClient snakeOnlineTestClient(ClientEventHandler clientEventHandler) {
        var snakeOnlineClient = new SnakeOnlineClientConfiguration().create(clientEventHandler);
        var websocketCreator = new WebsocketConfiguration().websocketCreator();
        var messagePublisher = new MessagePublisherCreator().create();
        var server = new ServerConfiguration().server(websocketCreator, messagePublisher);
        var gameplayCreator = new GameplayConfiguration().snakeGameplayCreator();
        var playersLimit = new UsersCountLimit(10);
        var roomCreator = new RoomConfiguration().roomCreator().createRoom(gameplayCreator, playersLimit, messagePublisher);
        return new SnakeOnlineTestClient(snakeOnlineClient, clientEventHandler, server);
    }
}