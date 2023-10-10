package com.noscompany.snake.game.online.host.server;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.DedicatedClientMessage;
import com.noscompany.snake.game.online.contract.messages.PublicClientMessage;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.commands.StartServer;
import com.noscompany.snake.game.online.host.server.ports.WebsocketCreator;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageSerializer;

public class ServerConfiguration {

    public Server server(WebsocketCreator websocketCreator, MessagePublisher messagePublisher, OnlineMessageSerializer onlineMessageSerializer, OnlineMessageDeserializer onlineMessageDeserializer) {
        var server = new ServerImpl(messagePublisher, websocketCreator, new ClosedWebsocket(), onlineMessageSerializer, onlineMessageDeserializer);
        var subscription = createSubscription(server);
        messagePublisher.subscribe(subscription);
        return server;
    }

    private Subscription createSubscription(Server server) {
        return new Subscription()
                .subscriberName("server")
//                server commands
                .toMessage(StartServer.class,  (StartServer msg) -> server.start(msg.getServerParams()))
//                host events
                .toMessage(HostGotShutdown.class, (HostGotShutdown msg) -> server.shutdown())
//                online messages
                .toMessage(PublicClientMessage.class, server::sendPublicClientMessage)
                .toMessage(DedicatedClientMessage.class, server::sendDedicatedClientMessage);
    }
}