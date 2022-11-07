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
public class GameCancelled implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = MessageType.GAME_CANCELLED;
    Collection<Snake> snakes;
    GridSize gridSize;
    Walls walls;
    Option<Position> foodPosition;

    public static GameCancelled gameCancelled(GameState gameState) {
        return new GameCancelled(
                gameState.getSnakes(),
                gameState.getGridSize(),
                gameState.getWalls(),
                gameState.getFoodPosition());
    }
}