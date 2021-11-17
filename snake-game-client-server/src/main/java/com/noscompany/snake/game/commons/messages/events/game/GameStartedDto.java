package com.noscompany.snake.game.commons.messages.events.game;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.Score;
import snake.game.core.dto.SnakeDto;
import snake.game.core.events.GameStarted;

import java.util.Collection;

import static com.noscompany.snake.game.commons.MessageDto.MessageType.GAME_STARTED;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameStartedDto implements MessageDto {
    MessageDto.MessageType messageType = GAME_STARTED;
    GridSize gridSize;
    Point foodPoint;
    Collection<SnakeDto> snakes;
    Score score;

    public GameStarted toGameEvent() {
        return new GameStarted(gridSize, foodPoint, snakes, score);
    }

    public static GameStartedDto dtoFrom(GameStarted event) {
        return new GameStartedDto(
                event.getGridSize(),
                event.getFoodPoint(),
                event.getSnakes(),
                event.getScore());
    }
}