package com.noscompany.snake.game.commons.messages.events.game;

import snake.game.core.events.*;

public class SnakeGameEventDtoCreator {

    public static GameCancelledDto asDto(GameCancelled event) {
        return new GameCancelledDto(
                event.getGridSize(),
                event.getSnakes(),
                event.getFoodPoint());
    }

    public static GameContinuesDto asDto(GameContinues event) {
        return new GameContinuesDto(
                event.getGridSize(),
                event.getSnakes(),
                event.getFoodPoint(),
                event.getScore());
    }

    public static GameFinishedDto asDto(GameFinished event) {
        return new GameFinishedDto(
                event.getGridSize(),
                event.getSnakes(),
                event.getFoodPoint(),
                event.getScore());
    }

    public static GameStartedDto asDto(GameStarted event) {
        return new GameStartedDto(
                event.getGridSize(),
                event.getFoodPoint(),
                event.getSnakes(),
                event.getScore());
    }

    public static TimeLeftToGameStartHasChangedDto asDto(TimeLeftToGameStartHasChanged event) {
        return new TimeLeftToGameStartHasChangedDto(
                event.getSecondsLeft(),
                event.getGridSize(),
                event.getSnakes(),
                event.getFoodPoint());
    }
}