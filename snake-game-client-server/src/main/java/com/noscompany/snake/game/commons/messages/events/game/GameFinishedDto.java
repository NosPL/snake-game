package com.noscompany.snake.game.commons.messages.events.game;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.Score;
import snake.game.core.dto.SnakeDto;
import snake.game.core.events.GameFinished;

import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameFinishedDto implements MessageDto {
    MessageDto.MessageType messageType = MessageDto.MessageType.GAME_FINISHED;
    GridSize gridSize;
    Collection<SnakeDto> snakes;
    Point foodPoint;
    Score score;

    public GameFinished toGameEvent() {
        return new GameFinished(snakes, gridSize, foodPoint, score);
    }

    public static GameFinishedDto dtoFrom(GameFinished event) {
        return new GameFinishedDto(
                event.getGridSize(),
                event.getSnakes(),
                event.getFoodPoint(),
                event.getScore());
    }
}