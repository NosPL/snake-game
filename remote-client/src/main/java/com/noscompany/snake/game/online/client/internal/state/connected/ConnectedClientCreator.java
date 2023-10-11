package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.internal.state.ClientState;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageSerializer;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ConnectedClientCreator {
    private final WebsocketCreator socketCreator = new WebsocketCreator();

    public Try<ClientState> create(HostAddress hostAddress,
                                          MessagePublisher messagePublisher,
                                          OnlineMessageDeserializer deserializer,
                                          OnlineMessageSerializer serializer) {
        var userId = new AtomicReference<>(new UserId(""));
        return socketCreator
                .create(hostAddress, messagePublisher, deserializer, serializer)
                .map(websocket -> new Connected(websocket, messagePublisher, userId, deserializer, serializer));
    }
}