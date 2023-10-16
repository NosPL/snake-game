package com.noscompany.snake.game.online.gameplay.gui.grid;

import com.noscompany.message.publisher.Subscription;
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
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameGridController extends AbstractController {
    @FXML
    private VBox gridVbox;
    private GameGrid gameGrid;
    private GridSize gridSize;
    private Walls walls;

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        super.doInitialize(location, resources);
        initializeGrid(GridSize._10x10, Walls.ON);
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(InitializePlaygroundToRemoteClient.class, this::initializePlayground)
                .toMessage(GameOptionsChanged.class, this::gameOptionsChanged)
                .toMessage(GameReinitialized.class, this::gameReinitialized)
                .toMessage(GameStartCountdown.class, this::gameStartCountdown)
                .toMessage(GameStarted.class, this::gameStarted)
                .toMessage(SnakesMoved.class, this::snakesMoved)
                .toMessage(GameFinished.class, this::gameFinished)
                .subscriberName("gameplay-grid-gui");
    }

    public void initializePlayground(InitializePlaygroundToRemoteClient event) {
        initializeGrid(
                event.getPlaygroundState().getGameState().getGridSize(),
                event.getPlaygroundState().getGameState().getWalls());
        Platform
                .runLater(() -> gameGrid.update(event.getPlaygroundState().getGameState().getSnakes()));
    }

    public void gameOptionsChanged(GameOptionsChanged event) {
        initializeGrid(
                event.getPlaygroundState().getGameState().getGridSize(),
                event.getPlaygroundState().getGameState().getWalls());
        Platform
                .runLater(() -> gameGrid.update(event.getPlaygroundState().getGameState().getSnakes()));
    }

    public void gameStartCountdown(GameStartCountdown event) {
        initializeGrid(event.getGridSize(), event.getWalls());
        Platform.runLater(() -> gameGrid.update(event.getSnakes(), event.getFoodPosition()));
    }

    public void gameStarted(GameStarted event) {
        initializeGrid(event.getGridSize(), event.getWalls());
        Platform.runLater(() -> gameGrid.update(event.getSnakes(), event.getFoodPosition()));
    }

    public void snakesMoved(SnakesMoved event) {
        initializeGrid(event.getGridSize(), event.getWalls());
        Platform.runLater(() -> gameGrid.update(event.getSnakes(), event.getFoodPosition()));
    }

    public void gameFinished(GameFinished event) {
        initializeGrid(event.getGridSize(), event.getWalls());
        Platform.runLater(() -> gameGrid.update(event.getSnakes(), event.getFoodPosition()));
    }

    public void localGameOptionsChanged(GameState gameState) {
        initializeGrid(gameState.getGridSize(), gameState.getWalls());
        Platform.runLater(() -> gameGrid.update(gameState.getSnakes()));
    }

    public void gameReinitialized(GameReinitialized event) {
        initializeGrid(event.getGameState().getGridSize(), event.getGameState().getWalls());
        Platform.runLater(() -> gameGrid.update(event.getGameState().getSnakes()));
    }

    public void failedToCreateGamePlay(GridSize gridSize, Walls walls) {
        initializeGrid(gridSize, walls);
    }

    private void initializeGrid(GridSize gridSize, Walls walls) {
        Platform.runLater(() -> {
            if (this.gridSize != gridSize || this.walls != walls) {
                gridVbox.getChildren().clear();
                gameGrid = GameGrid.Creator.createGrid(gridSize, walls);
                gridVbox.getChildren().add(gameGrid);
                this.gridSize = gridSize;
                this.walls = walls;
            }
        });
    }
}