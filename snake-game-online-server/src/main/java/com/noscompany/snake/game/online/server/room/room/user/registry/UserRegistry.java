package com.noscompany.snake.game.online.server.room.room.user.registry;

import com.noscompany.snake.game.commons.messages.events.room.FailedToEnterRoom;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.Value;

import java.util.Set;

public interface UserRegistry {

    boolean userNameIsValid(String userName);

    Option<FailedToEnterRoom> registerNewUser(String userId, String userName);

    Option<UserRemoved> removeUser(String userId);

    Option<String> findUserNameById(String userId);

    Set<String> getUserNames();

    int getUserCount();

    boolean containsId(String userId);

    @Value
    class UserRemoved {
        String userId;
        String userName;
    }
}