package com.noscompany.snake.game.commons.messages.events.game;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;
import snake.game.core.events.TimeLeftToGameStartHasChanged;

import java.util.Collection;

import static com.noscompany.snake.game.commons.MessageDto.MessageType.TIME_LEFT_TO_GAME_START_CHANGED;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class TimeLeftToGameStartHasChangedDto implements MessageDto {
    MessageType messageType = TIME_LEFT_TO_GAME_START_CHANGED;
    int secondsLeft;
    GridSize gridSize;
    Collection<SnakeDto> snakes;
    Point foodPoint;

    public TimeLeftToGameStartHasChanged toGameEvent() {
        return new TimeLeftToGameStartHasChanged(
                secondsLeft,
                snakes,
                gridSize,
                foodPoint);
    }

    public static TimeLeftToGameStartHasChangedDto dtoFrom(TimeLeftToGameStartHasChanged event) {
        return new TimeLeftToGameStartHasChangedDto(
                event.getSecondsLeft(),
                event.getGridSize(),
                event.getSnakes(),
                event.getFoodPoint());
    }
}