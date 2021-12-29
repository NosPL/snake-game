package com.noscompany.snake.game.online.server.room.room.commons;

import snake.game.core.SnakeGameEventHandler;
import snake.game.core.events.*;

public class SnakeGameNullHandler implements SnakeGameEventHandler {

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {

    }

    @Override
    public void handle(GameStarted event) {

    }

    @Override
    public void handle(GameContinues event) {

    }

    @Override
    public void handle(GameFinished event) {

    }

    @Override
    public void handle(GameCancelled event) {

    }

    @Override
    public void handle(GamePaused event) {

    }

    @Override
    public void handle(GameResumed event) {

    }
}