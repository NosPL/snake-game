package com.noscompany.snake.game.online.host.room.internal.user.registry;

import com.noscompany.snake.game.online.host.room.RoomCreator;

import java.util.concurrent.ConcurrentHashMap;

public class UserRegistryCreator {

    public static UserRegistry create(RoomCreator.PlayersLimit playersLimit) {
        return new UserRegistry(new ConcurrentHashMap<>(), playersLimit.getPlayersLimit());
    }
}