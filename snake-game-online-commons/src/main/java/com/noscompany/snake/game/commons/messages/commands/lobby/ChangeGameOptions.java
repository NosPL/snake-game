package com.noscompany.snake.game.commons.messages.commands.lobby;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Walls;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class ChangeGameOptions implements OnlineMessage {
    MessageType messageType = MessageType.CHANGE_GAME_OPTIONS;
    GridSize gridSize;
    GameSpeed gameSpeed;
    Walls walls;
}