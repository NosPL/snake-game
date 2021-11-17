package com.noscompany.snake.game.server.connected.users;

import com.noscompany.snake.game.commons.messages.events.lobby.NewUserAdded;
import com.noscompany.snake.game.commons.messages.events.lobby.UserRemoved;
import com.noscompany.snake.game.server.lobby.GameLobby;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
class ConnectedUsersImpl implements ConnectedUsers {
    private final Set<String> users;
    private final GameLobby gameLobby;

    @Override
    public synchronized Option<NewUserAdded> addNewUser(String userId) {
        var newUserAdded = users.add(userId);
        if (newUserAdded)
            return Option.of(new NewUserAdded(userId, new HashSet<>(users), gameLobby.getLobbyState()));
        return Option.none();
    }

    @Override
    public synchronized Option<UserRemoved> removeUser(String userId) {
        var userRemoved = users.remove(userId);
        if (userRemoved)
            return Option.of(new UserRemoved(userId, new HashSet<>(users)));
        return Option.none();
    }
}