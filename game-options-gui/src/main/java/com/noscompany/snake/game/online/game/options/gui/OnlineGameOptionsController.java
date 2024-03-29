package com.noscompany.snake.game.online.game.options.gui;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.game.options.InitializeGameOptions;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snake.game.online.gui.commons.Consumer3;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlineGameOptionsController extends AbstractController {
    @FXML
    private RadioButton gameSpeed1;
    @FXML
    private RadioButton gameSpeed2;
    @FXML
    private RadioButton gameSpeed3;
    @FXML
    private RadioButton gameSpeed4;
    @FXML
    private RadioButton gameSpeed5;
    @FXML
    private RadioButton gameSpeed6;
    @FXML
    private RadioButton gameSpeed7;
    @FXML
    private RadioButton gameSpeed8;

    @FXML
    private RadioButton gridSize1;
    @FXML
    private RadioButton gridSize2;
    @FXML
    private RadioButton gridSize3;
    @FXML
    private RadioButton gridSize4;

    @FXML
    private RadioButton wallsOn;
    @FXML
    private RadioButton wallsOff;

    private Consumer3<GridSize, GameSpeed, Walls> gameOptionsChangedAction = (grs, gs, w) -> {
    };

    public OnlineGameOptionsController onGameOptionsChanged(Consumer3<GridSize, GameSpeed, Walls> gameOptionsChangedAction) {
        this.gameOptionsChangedAction = gameOptionsChangedAction;
        return this;
    }

    @FXML
    public void gameOptionsChangedPressed() {
        log.debug("user changed options in gui");
        gameOptionsChangedAction.accept(gridSize(), gameSpeed(), walls());
    }

    public void gameOptionsChanged(GameOptionsChanged event) {
        log.debug("options got changed");
        update(event.getGameOptions());
    }

    private void initializeGameOptions(InitializeGameOptions command) {
        log.debug("game options got initialized");
        update(command.getGameOptions());
    }

    private void update(GameOptions gameOptions) {
        set(gameOptions.getGameSpeed());
        set(gameOptions.getGridSize());
        set(gameOptions.getWalls());
    }

    public GameSpeed gameSpeed() {
        if (gameSpeed1.isSelected())
            return GameSpeed.x1;
        if (gameSpeed2.isSelected())
            return GameSpeed.x2;
        if (gameSpeed3.isSelected())
            return GameSpeed.x3;
        if (gameSpeed4.isSelected())
            return GameSpeed.x4;
        if (gameSpeed5.isSelected())
            return GameSpeed.x5;
        if (gameSpeed6.isSelected())
            return GameSpeed.x6;
        if (gameSpeed7.isSelected())
            return GameSpeed.x7;
        if (gameSpeed8.isSelected())
            return GameSpeed.x8;
        throw new IllegalStateException("Non of game speed radio buttons is selected");
    }

    public GridSize gridSize() {
        if (gridSize1.isSelected())
            return GridSize._10x10;
        if (gridSize2.isSelected())
            return GridSize._15x15;
        if (gridSize3.isSelected())
            return GridSize._20x20;
        if (gridSize4.isSelected())
            return GridSize._25x25;
        throw new IllegalStateException("Non of grid size radio buttons is selected");
    }

    public Walls walls() {
        if (wallsOff.isSelected())
            return Walls.OFF;
        if (wallsOn.isSelected())
            return Walls.ON;
        throw new IllegalStateException("Non of walls radio buttons is selected");
    }

    private void set(GameSpeed gameSpeed) {
        if (gameSpeed == GameSpeed.x1)
            gameSpeed1.setSelected(true);
        else if (gameSpeed == GameSpeed.x2)
            gameSpeed2.setSelected(true);
        else if (gameSpeed == GameSpeed.x3)
            gameSpeed3.setSelected(true);
        else if (gameSpeed == GameSpeed.x4)
            gameSpeed4.setSelected(true);
        else if (gameSpeed == GameSpeed.x5)
            gameSpeed5.setSelected(true);
        else if (gameSpeed == GameSpeed.x6)
            gameSpeed6.setSelected(true);
        else if (gameSpeed == GameSpeed.x7)
            gameSpeed7.setSelected(true);
        else if (gameSpeed == GameSpeed.x8)
            gameSpeed8.setSelected(true);
    }

    private void set(GridSize gridSize) {
        if (gridSize == GridSize._10x10)
            gridSize1.setSelected(true);
        else if (gridSize == GridSize._15x15)
            gridSize2.setSelected(true);
        else if (gridSize == GridSize._20x20)
            gridSize3.setSelected(true);
        else if (gridSize == GridSize._25x25)
            gridSize4.setSelected(true);
    }

    private void set(Walls walls) {
        if (walls == Walls.ON)
            wallsOn.setSelected(true);
        else
            wallsOff.setSelected(true);
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(GameOptionsChanged.class, this::gameOptionsChanged)
                .toMessage(InitializeGameOptions.class, this::initializeGameOptions)
                .subscriberName("game-options-gui");
    }
}