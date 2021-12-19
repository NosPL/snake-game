package com.noscompany.snake.game.online.server.room.users;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.Value;

import java.util.Set;

public interface ConnectedUsers {

    Option<UserRemoved> removeUser(String userId);

    Either<UserNameNotFound, String> getUserName(String userId);

    Option<UserNameAlreadyInUse> add(String userId, String userName);

    Set<String> getUserNames();

    @Value
    class UserRemoved {
        String userId;
        String userName;
    }

    @Value
    class UserNameAlreadyInUse {
        String userId;
        String userName;
    }

    @Value
    class UserNameNotFound {
        String userId;
    }
}