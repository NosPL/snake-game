package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import com.noscompany.snake.game.server.lobby.GameLobbyState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;


@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class NewUserAdded implements MessageDto {
    MessageType messageType = MessageType.NEW_USER_ADDED;
    String userId;
    Set<String> users;
    GameLobbyState gameLobbyState;
}