package com.noscompany.snake.game.test.client;

import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientConfiguration;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snake.game.online.host.room.RoomConfiguration;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import com.noscompany.snake.game.online.host.mediator.MediatorConfiguration;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import com.noscompany.snake.game.online.websocket.WebsocketConfiguration;
import snake.game.gameplay.GameplayConfiguration;

public class SnakeOnlineTestClientConfiguration {

    public SnakeOnlineClient snakeOnlineTestClient(ClientEventHandler clientEventHandler) {
        var snakeOnlineClient = new SnakeOnlineClientConfiguration().create(clientEventHandler);
        var websocketCreator = new WebsocketConfiguration().websocketCreator();
        var server = new ServerConfiguration().server(websocketCreator);
        var messagePublisher = new NullMessagePublisher();
        var roomCreator = new RoomConfiguration().roomCreator();
        var snakeGameplayCreator = new GameplayConfiguration().snakeGameplayCreator();
        var playersLimit = new UsersCountLimit(10);
        var roomApiForRemoteClients = new MediatorConfiguration().roomApiForRemoteClients(messagePublisher, server, roomCreator, playersLimit, snakeGameplayCreator);
        return new SnakeOnlineTestClient(snakeOnlineClient, clientEventHandler, server, roomApiForRemoteClients);
    }
}