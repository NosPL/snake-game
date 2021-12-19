package com.noscompany.snake.game.online.server.room.users;

import java.util.concurrent.ConcurrentHashMap;

public class UsersCreator {

    public static ConnectedUsers create() {
        return new ConnectedUsersImpl(new ConcurrentHashMap<>());
    }
}
