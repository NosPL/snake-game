package com.noscompany.snake.game.online.client;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.client.internal.state.not.connected.Disconnected;
import com.noscompany.snake.game.online.contract.messages.network.YourIdGotInitialized;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageSerializer;

public class SnakeOnlineClientConfiguration {

    public SnakeOnlineClient create(MessagePublisher messagePublisher) {
        var serializer = OnlineMessageSerializer.instance();
        var deserializer = OnlineMessageDeserializer.instance();
        return create(messagePublisher, deserializer,serializer);
    }

    public SnakeOnlineClientImpl create(MessagePublisher messagePublisher,
                                        OnlineMessageDeserializer deserializer,
                                        OnlineMessageSerializer serializer) {
        var subscription = new Subscription()
                .toMessage(YourIdGotInitialized.class, RemoteClientIdHolder::yourIdGotInitialized)
                .subscriberName("remote client id holder");
        messagePublisher.subscribe(subscription);
        return new SnakeOnlineClientImpl(new Disconnected(messagePublisher, deserializer, serializer));
    }
}