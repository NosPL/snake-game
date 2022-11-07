package com.noscompany.snake.game.online.host.room.internal.user.registry;

import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
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

    int usersCount();

    @Value
    class UserRemoved {
        String userId;
        String userName;
    }
}