package com.noscompany.snakejavafxclient.components.local.game;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import snake.game.gameplay.Gameplay;

class NullGameplay implements Gameplay {

    @Override
    public void start() {

    }

    @Override
    public void changeSnakeDirection(PlayerNumber playerNumber, Direction direction) {

    }

    @Override
    public void killSnake(PlayerNumber playerNumber) {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public GameState getGameState() {
        return null;
    }
}
