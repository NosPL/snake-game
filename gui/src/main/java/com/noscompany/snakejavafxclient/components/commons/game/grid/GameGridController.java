package com.noscompany.snakejavafxclient.components.commons.game.grid;

import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStartCountdown;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStarted;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.SnakesMoved;
import com.noscompany.snake.game.online.contract.messages.playground.GameReinitialized;
import com.noscompany.snake.game.online.contract.messages.playground.InitializePlaygroundToRemoteClient;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import io.vavr.control.Option;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameGridController extends AbstractController {
    @FXML
    private VBox gridVbox;
    private GameGrid gameGrid;
    private Option<GridSize> gridSizeOption;
    private Option<Walls> wallsOption;

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        super.doInitialize(location, resources);
        initializeGrid(GridSize._10x10, Walls.ON);
    }

    public void handle(InitializePlaygroundToRemoteClient event) {
        if (gameGrid == null)
            initializeGrid(
                    event.getPlaygroundState().getGameState().getGridSize(),
                    event.getPlaygroundState().getGameState().getWalls());
        gameGrid.update(
                event.getPlaygroundState().getGameState().getSnakes());
    }

    public void handle(PlayerTookASeat event) {
        gridSizeOption.peek(gridSize ->
                wallsOption.peek(walls -> initializeGrid(gridSize, walls)));
    }

    public void handle(PlayerFreedUpASeat event) {
        gridSizeOption.peek(gridSize ->
                wallsOption.peek(walls -> initializeGrid(gridSize, walls)));
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

    public void handle(GameReinitialized event) {
        initializeGrid(event.getGameState().getGridSize(), event.getGameState().getWalls());
        gameGrid.update(event.getGameState().getSnakes());
    }

    public void failedToCreateGamePlay(GridSize gridSize, Walls walls) {
        initializeGrid(gridSize, walls);
    }

    private void initializeGrid(GridSize gridSize, Walls walls) {
        gridVbox.getChildren().clear();
        gameGrid = GameGrid.Creator.createGrid(gridSize, walls);
        gridVbox.getChildren().add(gameGrid);
        this.gridSizeOption = Option.of(gridSize);
        this.wallsOption = Option.of(walls);
    }
}