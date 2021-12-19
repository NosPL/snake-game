package com.noscompany.snake.game.online.server.room.users;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
class ConnectedUsersImpl implements ConnectedUsers {
    private final Map<String, String> userNamesById;

    @Override
    public synchronized Option<UserRemoved> removeUser(String userId) {
        var userName = userNamesById.remove(userId);
        if (userName != null)
            return Option.of(new UserRemoved(userId, userName));
        return Option.none();
    }

    @Override
    public synchronized Either<UserNameNotFound, String> getUserName(String userId) {
        return Option.of(userNamesById.get(userId))
                .toEither(new UserNameNotFound(userId));
    }

    @Override
    public synchronized Option<UserNameAlreadyInUse> add(String userId, String userName) {
        if (userNamesById.get(userId) != null)
            return Option.of(new UserNameAlreadyInUse(userId, userName));
        userNamesById.put(userId, userName);
        return Option.none();
    }

    @Override
    public synchronized Set<String> getUserNames() {
        return new HashSet<>(userNamesById.values());
    }
}