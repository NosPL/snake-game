package com.noscompany.snake.game.online.host.room.internal.user.registry;

import java.util.concurrent.ConcurrentHashMap;

public class UserRegistryCreator {

    public static UserRegistry create() {
        return new UserRegistryImpl(new ConcurrentHashMap<>());
    }
}