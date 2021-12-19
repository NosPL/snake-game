package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.OnlineMessage;
import com.noscompany.snake.game.commons.messages.dto.GameLobbyState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.SnakeNumber;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class PlayerFreedUpASeat implements OnlineMessage {
    MessageType messageType = MessageType.PLAYER_FREED_UP_SEAT;
    String userName;
    SnakeNumber freedUpSnakeNumber;
    GameLobbyState gameLobbyState;
}
