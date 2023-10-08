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

    public void update(GameState gameState) {
        initializeGrid(gameState.getGridSize(), gameState.getWalls());
        gameGrid.update(gameState.getSnakes(), gameState.getFoodPosition());
    }

    public void update(GridSize gridSize, Walls walls) {
        initializeGrid(gridSize, walls);
    }

    private void initializeGrid(GridSize gridSize, Walls walls) {
        gridVbox.getChildren().clear();
        if (gameGrid == null)
        gameGrid = GameGrid.Creator.createGrid(gridSize, walls);
        gridVbox.getChildren().add(gameGrid);
    }
}