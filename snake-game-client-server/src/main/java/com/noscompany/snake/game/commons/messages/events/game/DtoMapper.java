package com.noscompany.snake.game.commons.messages.events.game;

import snake.game.core.events.*;

public class DtoMapper {

    public static TimeLeftToGameStartHasChangedDto toDto(TimeLeftToGameStartHasChanged event) {
        return TimeLeftToGameStartHasChangedDto.dtoFrom(event);
    }

    public static GameStartedDto toDto(GameStarted event) {
        return GameStartedDto.dtoFrom(event);
    }

    public static GameContinuesDto toDto(GameContinues event) {
        return GameContinuesDto.dtoFrom(event);
    }

    public static GameFinishedDto toDto(GameFinished event) {
        return GameFinishedDto.dtoFrom(event);
    }

    public static GameCancelledDto toDto(GameCancelled event) {
        return GameCancelledDto.dtoFrom(event);
    }
}