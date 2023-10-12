package com.noscompany.snake.game.test.client;

import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.snake.game.online.chat.ChatConfiguration;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientConfiguration;
import com.noscompany.snake.game.online.contract.messages.user.registry.UsersCountLimit;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import com.noscompany.snake.game.online.online.contract.serialization.ObjectTypeMapper;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageSerializer;
import com.noscompany.snake.game.online.online.contract.serialization.object.type.mappers.*;
import com.noscompany.snake.game.online.playground.PlaygroundConfiguration;
import com.noscompany.snake.game.online.websocket.WebsocketConfiguration;
import com.noscompany.snake.online.user.registry.UserRegistryConfiguration;
import snake.game.gameplay.GameplayConfiguration;

import java.util.List;

public class SnakeOnlineTestClientConfiguration {

    public SnakeOnlineClient snakeOnlineTestClient(ClientEventHandler clientEventHandler) {
        var serializer = OnlineMessageSerializer.instance();
        var deserializer = OnlineMessageDeserializer.instance();
        var snakeOnlineClient = new SnakeOnlineClientConfiguration().create(clientEventHandler, new MessagePublisherCreator().create(), deserializer, serializer);
        var hostMessagePublisher = new MessagePublisherCreator().create();
        var websocketCreator = new WebsocketConfiguration().websocketCreator();
        var server = new ServerConfiguration()
                .server(websocketCreator, hostMessagePublisher, serializer, deserializer);
        new UserRegistryConfiguration().create(new UsersCountLimit(10), hostMessagePublisher);
        new ChatConfiguration().createChat(hostMessagePublisher);
        var gameplayCreator = new GameplayConfiguration().snakeGameplayCreator();
        new PlaygroundConfiguration().createPlayground(hostMessagePublisher, gameplayCreator);
        return new SnakeOnlineTestClient(snakeOnlineClient, clientEventHandler, server);
    }
}