package com.noscompany.snake.game.online.playground.test.commons;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import snake.game.gameplay.ports.GameplayEventHandler;

public class GameplayIdleEventHandler implements GameplayEventHandler {

    @Override
    public void handle(GameStartCountdown event) {

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