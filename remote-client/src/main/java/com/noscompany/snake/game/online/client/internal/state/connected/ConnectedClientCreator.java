package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.RemoteClientIdHolder;
import com.noscompany.snake.game.online.client.StartingClientError;
import com.noscompany.snake.game.online.client.internal.state.ClientState;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageSerializer;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectedClientCreator {
    private final WebsocketCreator socketCreator = new WebsocketCreator();

    public Either<StartingClientError, ClientState> create(HostAddress hostAddress,
                                                          MessagePublisher messagePublisher,
                                                          OnlineMessageDeserializer deserializer,
                                                          OnlineMessageSerializer serializer) {
        RemoteClientIdHolder.reset();
        return socketCreator
                .create(hostAddress, messagePublisher, deserializer, serializer)
                .map(websocket -> new Connected(websocket, messagePublisher, deserializer, serializer));
    }
}