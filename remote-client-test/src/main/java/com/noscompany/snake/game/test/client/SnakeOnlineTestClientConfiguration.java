package com.noscompany.snake.game.test.client;

import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientConfiguration;
import com.noscompany.snake.game.online.contract.messages.room.PlayersLimit;
import com.noscompany.snake.game.online.host.room.RoomConfiguration;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import com.noscompany.snake.game.online.host.mediator.MediatorConfiguration;
import com.noscompany.snake.game.online.websocket.WebsocketConfiguration;
import io.vavr.control.Option;
import snake.game.gameplay.GameplayConfiguration;

public class SnakeOnlineTestClientConfiguration {

    public SnakeOnlineClient snakeOnlineTestClient(ClientEventHandler clientEventHandler) {
        var snakeOnlineClient = new SnakeOnlineClientConfiguration().create(clientEventHandler);
        var websocketCreator = new WebsocketConfiguration().websocketCreator();
        var server = new ServerConfiguration().server(websocketCreator);
        var nullHostEventHandler = new NullHostEventHandler();
        var roomCreator = new RoomConfiguration().roomCreator();
        var snakeGameplayCreator = new GameplayConfiguration().snakeGameplayCreator();
        var playersLimit = new PlayersLimit(10);
        var roomMediator = new MediatorConfiguration().mediator(nullHostEventHandler, server, roomCreator, playersLimit, snakeGameplayCreator);
        return new SnakeOnlineTestClient(snakeOnlineClient, clientEventHandler, server, roomMediator, Option.none());
    }
}