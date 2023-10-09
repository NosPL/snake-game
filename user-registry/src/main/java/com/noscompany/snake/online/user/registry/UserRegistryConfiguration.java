package com.noscompany.snake.online.user.registry;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.user.registry.EnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.contract.messages.user.registry.UsersCountLimit;
import com.noscompany.snake.game.online.contract.messages.server.events.RemoteClientDisconnected;

import java.util.concurrent.ConcurrentHashMap;

public class UserRegistryConfiguration {

    public UserRegistry create(UsersCountLimit usersCountLimit, MessagePublisher messagePublisher) {
        int userNameMaxLength = 15;
        var userRegistry = new UserRegistry(new ConcurrentHashMap<>(), usersCountLimit.getLimit(), userNameMaxLength);
        var subscription = createSubscription(userRegistry);
        messagePublisher.subscribe(subscription);
        return userRegistry;
    }

    private Subscription createSubscription(UserRegistry userRegistry) {
        return new Subscription()
                .toMessage(EnterRoom.class, (EnterRoom command) -> userRegistry.enterRoom(command.getUserId(), new UserName(command.getUserName())))
                .toMessage(RemoteClientDisconnected.class, (RemoteClientDisconnected msg) -> userRegistry.leaveTheRoom(msg.getRemoteClientId()))
                .toMessage(HostGotShutdown.class, (HostGotShutdown msg) -> userRegistry.removeAllUsers())
                .subscriberName("user registry");
    }
}