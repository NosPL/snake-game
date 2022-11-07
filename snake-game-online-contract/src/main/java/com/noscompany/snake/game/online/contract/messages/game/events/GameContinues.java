package com.noscompany.snake.game.online.contract.messages.game.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameContinues implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = MessageType.SNAKES_MOVED;
    GridSize gridSize;
    Walls walls;
    Option<Position> foodPosition;
    Collection<Snake> snakes;
    Score score;

    public static GameContinues createEvent(GameState gameState) {
        return new GameContinues(
                gameState.getGridSize(),
                gameState.getWalls(),
                gameState.getFoodPosition(),
                gameState.getSnakes(),
                gameState.getScore());
    }
}