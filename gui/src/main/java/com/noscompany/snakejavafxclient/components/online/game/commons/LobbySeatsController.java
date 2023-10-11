package com.noscompany.snakejavafxclient.components.online.game.commons;

import com.noscompany.snake.game.online.contract.messages.seats.AdminId;
import com.noscompany.snake.game.online.contract.messages.seats.Seat;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snakejavafxclient.SnakesColors;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import io.vavr.control.Option;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;

import static com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber.*;

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

    private Consumer<PlayerNumber> takeASeatAction = snakeNumber -> {
    };
    private Runnable freeUpASeatAction = () -> {
    };

    public LobbySeatsController onTakeASeatButtonPress(Consumer<PlayerNumber> takeASeatAction) {
        this.takeASeatAction = takeASeatAction;
        return this;
    }

    public LobbySeatsController onFreeUpASeatButtonPress(Runnable freeUpASeatAction) {
        this.freeUpASeatAction = freeUpASeatAction;
        return this;
    }

    @FXML
    public void takeFirstSeat() {
        takeASeatAction.accept(_1);
    }

    @FXML
    public void takeSecondSeat() {
        takeASeatAction.accept(PlayerNumber._2);
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

    public void update(Set<Seat> seats, Option<AdminId> adminId) {
        resetSeatsLabels();
        updateSeats(seats, adminId);
    }

    private void updateSeats(Set<Seat> seats, Option<AdminId> adminId) {
        seats.forEach(seat -> seatTook(seat.getUserName(), seat.getPlayerNumber(), seat.isAdmin(adminId)));
    }

    private void seatTook(Option<UserName> userName, PlayerNumber playerNumber, boolean isAdmin) {
        findSeatLabelBy(playerNumber).setText(format(userName, isAdmin));
    }

    private Label findSeatLabelBy(PlayerNumber playerNumber) {
        if (playerNumber == _1) {
            return firstSeatLabel;
        } else if (playerNumber == PlayerNumber._2) {
            return secondSeatLabel;
        } else if (playerNumber == _3) {
            return thirdSeatLabel;
        } else if (playerNumber == _4) {
            return fourthSeatLabel;
        } else
            throw new RuntimeException("Cannot find seat label for player number: " + playerNumber);
    }

    private String format(Option<UserName> userName, boolean isAdmin) {
        return userName
                .map(UserName::getName)
                .map(name -> name + adminSuffix(isAdmin))
                .getOrElse("");
    }

    private String adminSuffix(boolean isAdmin) {
        return isAdmin ? " (admin)" : "";
    }

    private void resetSeatsLabels() {
        firstSeatLabel.setText("");
        secondSeatLabel.setText("");
        thirdSeatLabel.setText("");
        fourthSeatLabel.setText("");
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