package com.noscompany.snake.game.online.local.game;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.gui.commons.SnakesColors;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snake.game.online.local.game.edit.snake.name.EditSnakeNameConfiguration;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import snake.game.gameplay.dto.GameplayParams;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class GameOptionsController extends AbstractController {
    @FXML
    private VBox vBox;
    @FXML
    private CheckBox playerNumber1;
    @FXML
    private CheckBox playerNumber2;
    @FXML
    private CheckBox playerNumber3;
    @FXML
    private CheckBox playerNumber4;

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

    private LocalSnakeGame localSnakeGame;

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        playerNumber1.setTextFill(SnakesColors.get(PlayerNumber._1));
        playerNumber2.setTextFill(SnakesColors.get(PlayerNumber._2));
        playerNumber3.setTextFill(SnakesColors.get(PlayerNumber._3));
        playerNumber4.setTextFill(SnakesColors.get(PlayerNumber._4));
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription();
    }

    public void set(LocalSnakeGame localSnakeGame) {
        this.localSnakeGame = localSnakeGame;
    }

    @FXML
    public void gameOptionsChanged() {
        localSnakeGame.updateGameGrid();
    }

    @FXML
    public void editPlayer1() {
        EditSnakeNameConfiguration.run(PlayerNumber._1, playerNumber1.getText());
    }

    @FXML
    public void editPlayer2() {
        EditSnakeNameConfiguration.run(PlayerNumber._2, playerNumber2.getText());
    }

    @FXML
    public void editPlayer3() {
        EditSnakeNameConfiguration.run(PlayerNumber._3, playerNumber3.getText());
    }

    @FXML
    public void editPlayer4() {EditSnakeNameConfiguration.run(PlayerNumber._4, playerNumber4.getText());
    }

    public void disable() {
        vBox.setDisable(true);
    }

    public void enable() {
        vBox.setDisable(false);
    }

    public Set<PlayerNumber> playerNumbers() {
        var result = new HashSet<PlayerNumber>();
        if (playerNumber1.isSelected())
            result.add(PlayerNumber._1);
        if (playerNumber2.isSelected())
            result.add(PlayerNumber._2);
        if (playerNumber3.isSelected())
            result.add(PlayerNumber._3);
        if (playerNumber4.isSelected())
            result.add(PlayerNumber._4);
        return result;
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

    void snakeNameUpdated(PlayerNumber playerNumber, String newName) {
        CheckBox player = getCheckBox(playerNumber);
        player.setText(newName);
    }

    private CheckBox getCheckBox(PlayerNumber playerNumber) {
        if (playerNumber == PlayerNumber._1)
            return playerNumber1;
        if (playerNumber == PlayerNumber._2)
            return playerNumber2;
        if (playerNumber == PlayerNumber._3)
            return playerNumber3;
        else
            return playerNumber4;
    }

    public GameplayParams getGameplayParams() {
        return new GameplayParams(playerNumbers(), gameSpeed(), gridSize(), walls(), CountdownTime.inSeconds(3));
    }
}