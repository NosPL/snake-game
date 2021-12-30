package com.noscompany.snake.game.commons.messages.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class RoomState {
    int userLimit;
    Set<String> users;
    LobbyState lobbyState;

    public boolean isFull() {
        return userLimit <= users.size();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }
}