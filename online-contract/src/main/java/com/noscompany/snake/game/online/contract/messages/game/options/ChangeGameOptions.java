package com.noscompany.snake.game.online.contract.messages.game.options;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class ChangeGameOptions implements OnlineMessage {
    MessageType messageType = MessageType.CHANGE_GAME_OPTIONS;
    GridSize gridSize;
    GameSpeed gameSpeed;
    Walls walls;

    public GameOptions getOptions() {
        return new GameOptions(gridSize, gameSpeed, walls);
    }
}