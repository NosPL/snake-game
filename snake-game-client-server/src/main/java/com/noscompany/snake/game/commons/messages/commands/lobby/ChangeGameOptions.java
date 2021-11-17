package com.noscompany.snake.game.commons.messages.commands.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Walls;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ChangeGameOptions implements MessageDto {
    MessageType messageType = MessageType.CHANGE_GAME_OPTIONS;
    GridSize gridSize;
    GameSpeed gameSpeed;
    Walls walls;
}