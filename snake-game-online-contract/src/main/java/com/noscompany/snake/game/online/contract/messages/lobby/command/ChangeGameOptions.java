package com.noscompany.snake.game.online.contract.messages.lobby.command;

import com.noscompany.snake.game.online.contract.messages.game.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.Walls;

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