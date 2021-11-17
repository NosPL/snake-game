package com.noscompany.snake.game.commons.messages.events.game;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.Score;
import snake.game.core.dto.SnakeDto;
import snake.game.core.events.GameContinues;

import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameContinuesDto implements MessageDto {
    MessageType messageType = MessageType.GAME_CONTINUES;
    GridSize gridSize;
    Collection<SnakeDto> snakes;
    Point foodPoint;
    Score score;

    public GameContinues toGameEvent() {
        return new GameContinues(gridSize, foodPoint, snakes, score);
    }

    public static GameContinuesDto dtoFrom(GameContinues event) {
        return new GameContinuesDto(
                event.getGridSize(),
                event.getSnakes(),
                event.getFoodPoint(),
                event.getScore());
    }
}