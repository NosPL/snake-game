package com.noscompany.snake.game.online.host.room.internal.user.registry;

import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;

import java.util.concurrent.ConcurrentHashMap;

public class UserRegistryCreator {

    public static UserRegistry create(UsersCountLimit usersCountLimit) {
        return new UserRegistry(new ConcurrentHashMap<>(), usersCountLimit.getLimit());
    }
}