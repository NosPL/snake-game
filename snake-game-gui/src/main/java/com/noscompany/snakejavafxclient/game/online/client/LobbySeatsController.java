package com.noscompany.snakejavafxclient.game.online.client;

import com.noscompany.snake.game.commons.messages.dto.GameLobbyState;
import com.noscompany.snake.game.commons.messages.dto.LobbyAdmin;
import com.noscompany.snakejavafxclient.commons.AbstractController;
import com.noscompany.snakejavafxclient.game.SnakesColors;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import snake.game.core.dto.SnakeNumber;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static snake.game.core.dto.SnakeNumber.*;

public class LobbySeatsController extends AbstractController {
    private static final String ADMIN = " (admin)";
    @FXML
    private Label firstSeatLabel;
    @FXML
    private Label secondSeatLabel;
    @FXML
    private Label thirdSeatLabel;
    @FXML
    private Label fourthSeatLabel;
    @FXML
    private Button firstSeatButton;
    @FXML
    private Button secondSeatButton;
    @FXML
    private Button thirdSeatButton;
    @FXML
    private Button fourthSeatButton;

    private Consumer<SnakeNumber> takeASeatAction = snakeNumber -> {
    };
    private Runnable freeUpASeatAction = () -> {
    };

    public LobbySeatsController onTakeASeat(Consumer<SnakeNumber> takeASeatAction) {
        this.takeASeatAction = takeASeatAction;
        return this;
    }

    public LobbySeatsController onFreeUpASeat(Runnable freeUpASeatAction) {
        this.freeUpASeatAction = freeUpASeatAction;
        return this;
    }

    @FXML
    public void takeFirstSeat() {
        takeASeatAction.accept(SnakeNumber._1);
    }

    @FXML
    public void takeSecondSeat() {
        takeASeatAction.accept(SnakeNumber._2);
    }

    @FXML
    public void takeThirdSeat() {
        takeASeatAction.accept(_3);
    }

    @FXML
    public void takeFourthSeat() {
        takeASeatAction.accept(_4);
    }

    @FXML
    public void freeUpASeat() {
        freeUpASeatAction.run();
    }

    public void update(GameLobbyState gameLobbyState) {
        resetSeatsLabels();
        updateSeats(gameLobbyState.getJoinedPlayers());
        gameLobbyState
                .getAdmin()
                .peek(this::update);
    }

    private void update(LobbyAdmin lobbyAdmin) {
        SnakeNumber snakeNumber = lobbyAdmin.getSnakeNumber();
        if (snakeNumber == _1)
            firstSeatLabel.setText(firstSeatLabel.getText() + ADMIN);
        if (snakeNumber == _2)
            secondSeatLabel.setText(secondSeatLabel.getText() + ADMIN);
        if (snakeNumber == _3)
            thirdSeatLabel.setText(thirdSeatLabel.getText() + ADMIN);
        if (snakeNumber == _4)
            fourthSeatLabel.setText(fourthSeatLabel.getText() + ADMIN);
    }

    public void updateSeats(Map<String, SnakeNumber> joinedPlayers) {
        joinedPlayers.forEach(this::seatTook);
    }

    private void seatTook(String userName, SnakeNumber snakeNumber) {
        if (snakeNumber == _1) {
            firstSeatLabel.setText("p1: " + format(userName));
        } else if (snakeNumber == SnakeNumber._2) {
            secondSeatLabel.setText("p2: " + format(userName));
        } else if (snakeNumber == _3) {
            thirdSeatLabel.setText("p3: " + format(userName));
        } else if (snakeNumber == _4) {
            fourthSeatLabel.setText("p4: " + format(userName));
        }
    }

    private String format(String userId) {
        if (userId.length() < 11)
            return userId;
        else
            return userId.substring(0, 9).concat("...");
    }

    public void seatFreedUp(SnakeNumber snakeNumber) {
        if (snakeNumber == _1) {
            firstSeatLabel.setText("p1:");
        } else if (snakeNumber == SnakeNumber._2) {
            secondSeatLabel.setText("p2:");
        } else if (snakeNumber == _3) {
            thirdSeatLabel.setText("p3:");
        } else if (snakeNumber == _4) {
            fourthSeatLabel.setText("p4:");
        }
    }

    private void resetSeatsLabels() {
        firstSeatLabel.setText("p1:");
        secondSeatLabel.setText("p2:");
        thirdSeatLabel.setText("p3:");
        fourthSeatLabel.setText("p4:");
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        setColors();
        resetSeatsLabels();
    }

    private void setColors() {
        firstSeatLabel.setTextFill(SnakesColors.get(_1));
        firstSeatButton.setTextFill(SnakesColors.get(_1));
        secondSeatLabel.setTextFill(SnakesColors.get(_2));
        secondSeatButton.setTextFill(SnakesColors.get(_2));
        thirdSeatLabel.setTextFill(SnakesColors.get(_3));
        thirdSeatButton.setTextFill(SnakesColors.get(_3));
        fourthSeatLabel.setTextFill(SnakesColors.get(_4));
        fourthSeatButton.setTextFill(SnakesColors.get(_4));
    }
}