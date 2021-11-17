package com.noscompany.snake.game.commons.messages.events.game;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;
import snake.game.core.events.GameCancelled;

import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameCancelledDto implements MessageDto {
    MessageType messageType = MessageType.GAME_CANCELLED;
    GridSize gridSize;
    Collection<SnakeDto> snakes;
    Point foodPoint;

    public GameCancelled toGameEvent() {
        return new GameCancelled(snakes, gridSize, foodPoint);
    }

    public static GameCancelledDto dtoFrom(GameCancelled event) {
        return new GameCancelledDto(
                event.getGridSize(),
                event.getSnakes(),
                event.getFoodPoint());
    }
}