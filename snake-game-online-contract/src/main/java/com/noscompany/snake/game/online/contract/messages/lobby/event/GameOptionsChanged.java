package com.noscompany.snake.game.online.contract.messages.lobby.event;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameOptionsChanged implements OnlineMessage {
    MessageType messageType = MessageType.GAME_OPTIONS_CHANGED;
    LobbyState lobbyState;
}