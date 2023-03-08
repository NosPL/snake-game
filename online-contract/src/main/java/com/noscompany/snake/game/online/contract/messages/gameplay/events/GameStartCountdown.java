package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
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
public class GameStartCountdown implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = MessageType.TIME_LEFT_TO_GAME_START_CHANGED;
    int secondsLeft;
    Collection<Snake> snakes;
    GridSize gridSize;
    Walls walls;
    Option<Position> foodPosition;
    Score score;

    public static GameStartCountdown timeLeftToGameStartHasChanged(int secondsLeft, GameState currentState) {
        return new GameStartCountdown(
                secondsLeft,
                currentState.getSnakes(),
                currentState.getGridSize(),
                currentState.getWalls(),
                currentState.getFoodPosition(),
                currentState.getScore());
    }
}