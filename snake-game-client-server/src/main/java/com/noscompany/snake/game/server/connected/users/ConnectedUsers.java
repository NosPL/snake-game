package com.noscompany.snake.game.server.connected.users;

import com.noscompany.snake.game.commons.messages.events.lobby.NewUserAdded;
import com.noscompany.snake.game.commons.messages.events.lobby.UserRemoved;
import com.noscompany.snake.game.server.lobby.GameLobby;
import io.vavr.control.Option;

import java.util.HashSet;

public interface ConnectedUsers {
    Option<NewUserAdded> addNewUser(String userId);

    Option<UserRemoved> removeUser(String userId);

    static ConnectedUsers instance(GameLobby gameLobby) {
        return new ConnectedUsersImpl(new HashSet<>(), gameLobby);
    }
}
