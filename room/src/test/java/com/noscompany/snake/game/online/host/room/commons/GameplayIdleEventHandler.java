package com.noscompany.snake.game.online.host.room.commons;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import snake.game.gameplay.GameplayEventHandler;

public class GameplayIdleEventHandler implements GameplayEventHandler {

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {

    }

    @Override
    public void handle(GameStarted event) {

    }

    @Override
    public void handle(SnakesMoved event) {

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