package com.noscompany.snakejavafxclient.game.grid.controller;

import com.noscompany.snakejavafxclient.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import snake.game.core.dto.GameState;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class GameGridController extends AbstractController {
    @FXML
    private VBox gridVbox;
    private GameGrid gameGrid;

    public void initializeGrid(GridSize gridSize) {
        gridVbox.getChildren().clear();
        gameGrid = GameGrid.Creator.createGrid(gridSize);
        gridVbox.getChildren().add(gameGrid);
    }

    public void updateGrid(Collection<SnakeDto> snakes, Point foodPoint) {
        gameGrid.update(snakes, foodPoint);
    }

    public void updateGrid(Collection<SnakeDto> snakes) {
        gameGrid.update(snakes);
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
    }

    public void update(GameState gameState) {
        initializeGrid(gameState.getGridSize());
        updateGrid(gameState.getSnakes());
    }
}