package com.noscompany.snakejavafxclient.components.commons.game.grid;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import io.vavr.control.Option;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.Collection;

public class GameGridController extends AbstractController {
    @FXML
    private VBox gridVbox;
    private GameGrid gameGrid;

    public void initializeGrid(GridSize gridSize, Walls walls) {
        gridVbox.getChildren().clear();
        gameGrid = GameGrid.Creator.createGrid(gridSize, walls);
        gridVbox.getChildren().add(gameGrid);
    }

    public void updateGrid(Collection<Snake> snakes, Option<Position> foodPosition) {
        gameGrid.update(snakes, foodPosition);
    }

    public void updateGrid(Collection<Snake> snakes) {
        gameGrid.update(snakes);
    }

    public void update(GameState gameState) {
        initializeGrid(gameState.getGridSize(), gameState.getWalls());
        updateGrid(gameState.getSnakes());
    }
}