package com.noscompany.snakejavafxclient.components.commons.game.grid;

import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStartCountdown;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStarted;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.SnakesMoved;
import com.noscompany.snake.game.online.contract.messages.mediator.InitializeRemoteClientState;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import io.vavr.control.Option;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class GameGridController extends AbstractController {
    @FXML
    private VBox gridVbox;
    private GameGrid gameGrid;

    public void handle(InitializeRemoteClientState event) {
        if (gameGrid == null)
            initializeGrid(
                    event.getRoomState().getPlaygroundState().getGameState().getGridSize(),
                    event.getRoomState().getPlaygroundState().getGameState().getWalls());
        gameGrid.update(
                event.getRoomState().getPlaygroundState().getGameState().getSnakes());
    }

    public void handle(NewUserEnteredRoom event) {
        if (gameGrid == null)
            initializeGrid(
                    event.getRoomState().getPlaygroundState().getGameState().getGridSize(),
                    event.getRoomState().getPlaygroundState().getGameState().getWalls());
        gameGrid.update(
                event.getRoomState().getPlaygroundState().getGameState().getSnakes());
    }

    public void handle(PlayerTookASeat event) {
            initializeGrid(
                    event.getPlaygroundState().getGameState().getGridSize(),
                    event.getPlaygroundState().getGameState().getWalls());
        gameGrid.update(
                event.getPlaygroundState().getGameState().getSnakes());
    }

    public void handle(PlayerFreedUpASeat event) {
            initializeGrid(
                    event.getPlaygroundState().getGameState().getGridSize(),
                    event.getPlaygroundState().getGameState().getWalls());
        gameGrid.update(
                event.getPlaygroundState().getGameState().getSnakes());
    }

    public void handle(GameOptionsChanged event) {
            initializeGrid(
                    event.getPlaygroundState().getGameState().getGridSize(),
                    event.getPlaygroundState().getGameState().getWalls());
        gameGrid.update(
                event.getPlaygroundState().getGameState().getSnakes());
    }

    public void handle(GameStartCountdown event) {
        if (gameGrid == null)
            initializeGrid(event.getGridSize(), event.getWalls());
        gameGrid.update(event.getSnakes(), event.getFoodPosition());
    }

    public void handle(GameStarted event) {
        if (gameGrid == null)
            initializeGrid(event.getGridSize(), event.getWalls());
        gameGrid.update(event.getSnakes(), event.getFoodPosition());
    }

    public void handle(SnakesMoved event) {
        if (gameGrid == null)
            initializeGrid(event.getGridSize(), event.getWalls());
        gameGrid.update(event.getSnakes(), event.getFoodPosition());
    }

    public void handle(GameFinished event) {
        if (gameGrid == null)
            initializeGrid(event.getGridSize(), event.getWalls());
        gameGrid.update(event.getSnakes(), event.getFoodPosition());
    }

    public void localGameOptionsChanged(GameState gameState) {
        initializeGrid(gameState.getGridSize(), gameState.getWalls());
        gameGrid.update(gameState.getSnakes());
    }

    public void failedToCreateGamePlay(GridSize gridSize, Walls walls) {
        initializeGrid(gridSize, walls);
    }

    private void initializeGrid(GridSize gridSize, Walls walls) {
        gridVbox.getChildren().clear();
        gameGrid = GameGrid.Creator.createGrid(gridSize, walls);
        gridVbox.getChildren().add(gameGrid);
    }
}