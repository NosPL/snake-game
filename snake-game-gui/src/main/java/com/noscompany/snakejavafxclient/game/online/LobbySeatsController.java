package com.noscompany.snakejavafxclient.game.online;

import com.noscompany.snakejavafxclient.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import snake.game.core.dto.SnakeNumber;

import java.util.Map;
import java.util.function.Consumer;

import static snake.game.core.dto.SnakeNumber._1;

public class LobbySeatsController extends AbstractController {
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
        takeASeatAction.accept(SnakeNumber._3);
    }

    @FXML
    public void takeFourthSeat() {
        takeASeatAction.accept(SnakeNumber._4);
    }

    @FXML
    public void freeUpASeat() {
        freeUpASeatAction.run();
    }

    public void updateSeats(Map<String, SnakeNumber> joinedPlayers) {
        resetSeatsLabels();
        joinedPlayers.forEach(this::seatTook);
    }

    private void seatTook(String userId, SnakeNumber snakeNumber) {
        if (snakeNumber == _1) {
            firstSeatLabel.setText("p1: " + format(userId));
        } else if (snakeNumber == SnakeNumber._2) {
            secondSeatLabel.setText("p2: " + format(userId));
        } else if (snakeNumber == SnakeNumber._3) {
            thirdSeatLabel.setText("p3: " + format(userId));
        } else if (snakeNumber == SnakeNumber._4) {
            fourthSeatLabel.setText("p4: " + format(userId));
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
        } else if (snakeNumber == SnakeNumber._3) {
            thirdSeatLabel.setText("p3:");
        } else if (snakeNumber == SnakeNumber._4) {
            fourthSeatLabel.setText("p4:");
        }
    }

    private void resetSeatsLabels() {
        firstSeatLabel.setText("p1:");
        secondSeatLabel.setText("p2:");
        thirdSeatLabel.setText("p3:");
        fourthSeatLabel.setText("p4:");
    }
}