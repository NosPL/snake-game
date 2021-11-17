package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import com.noscompany.snake.game.server.lobby.GameLobbyState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;


@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class NewUserConnectedAsAdmin implements MessageDto {
    MessageType messageType = MessageType.NEW_USER_ADDED_AS_ADMIN;
    String userId;
    GameLobbyState gameLobbyState;
}