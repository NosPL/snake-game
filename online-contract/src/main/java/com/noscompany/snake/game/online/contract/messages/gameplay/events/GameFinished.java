package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.PublicClientMessage;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameFinished implements GameEvent, PublicClientMessage {
    OnlineMessage.MessageType messageType = MessageType.GAME_FINISHED;
    Collection<Snake> snakes;
    GridSize gridSize;
    Walls walls;
    Option<Position> foodPosition;
    Score score;

    public static GameFinished createEvent(GameState gameState) {
        return new GameFinished(
                gameState.getSnakes(),
                gameState.getGridSize(),
                gameState.getWalls(),
                gameState.getFoodPosition(),
                gameState.getScore());
    }
}