package com.noscompany.snake.game.online.host.room.internal.user.registry;

import com.noscompany.snake.game.online.contract.messages.room.PlayersLimit;

import java.util.concurrent.ConcurrentHashMap;

public class UserRegistryCreator {

    public static UserRegistry create(PlayersLimit playersLimit) {
        return new UserRegistry(new ConcurrentHashMap<>(), playersLimit.getPlayersLimit());
    }
}