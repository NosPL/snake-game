package com.noscompany.snake.game.online.remote.client.gui;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.client.*;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import io.vavr.control.Either;
import io.vavr.control.Try;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static com.noscompany.snake.game.online.client.StartingClientError.PORT_IS_NOT_A_NUMBER;
import static com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom.Reason.USER_ALREADY_IN_THE_ROOM;

public class JoinGameController extends AbstractController {
    @FXML
    private TextField ipTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField playerNameTextField;
    @FXML
    private Label errorMessageLabel;
    private SnakeOnlineClient snakeOnlineClient;

    public void setSnakeOnlineClient(SnakeOnlineClient snakeOnlineClient) {
        this.snakeOnlineClient = snakeOnlineClient;
    }

    public void disconnect() {
        snakeOnlineClient.disconnect();
    }

    @FXML
    public void joinGame() {
        getIpAddress()
                .peek(this::joinGame)
                .peekLeft(this::handleError);
    }

    private void joinGame(HostAddress hostAddress) {
        if (!snakeOnlineClient.isConnected())
            snakeOnlineClient.connect(hostAddress);
        else
            enterRoom();
    }

    public void connectionEstablished(ConnectionEstablished event) {
        enterRoom();
    }

    public void enterRoom() {
        errorMessageLabel.setText("connection established, entering room...");
        var userNameString = playerNameTextField.getText();
        snakeOnlineClient.enterTheRoom(new UserName(userNameString));
    }

    public void newUserEnteredRoom(NewUserEnteredRoom event) {
        if (itsYourId(event.getUserId())) {
            JoinGameStage.get().close();
            SnakeOnlineClientStage.get().show();
        }
    }

    private Boolean itsYourId(UserId userId) {
        return RemoteClientIdHolder
                .userId()
                .map(userId::equals)
                .getOrElse(false);
    }

    public void failedToEnterRoom(FailedToEnterRoom event) {
        String errorMessage = asString(event.getReason());
        Platform.runLater(() -> errorMessageLabel.setText(errorMessage));
        if (event.getReason() == USER_ALREADY_IN_THE_ROOM) {
            JoinGameStage.get().close();
            SnakeOnlineClientStage.get().show();
        }
    }

    public void sendClientMessageError(SendClientMessageError sendClientMessageError) {
        Platform.runLater(() -> errorMessageLabel.setText(asString(sendClientMessageError)));
    }

    public void startingClientError(StartingClientError startingClientError) {
        Platform.runLater(() -> errorMessageLabel.setText(asString(startingClientError)));
    }

    private Either<StartingClientError, HostAddress> getIpAddress() {
        var portString = portTextField.getText();
        if (!isNumber(portString))
            return Either.left(PORT_IS_NOT_A_NUMBER);
        var ipString = ipTextField.getText();
        int port = Integer.parseInt(portString);
        return Either.right(new HostAddress(ipString, port));
    }

    private boolean isNumber(String portString) {
        return Try.of(() -> Integer.parseInt(portString)).isSuccess();
    }

    private void handleError(StartingClientError error) {
        errorMessageLabel.setText(asString(error));
    }

    private String asString(Enum anEnum) {
        return anEnum.toString().toLowerCase().replace("_", " ");
    }

    public void connectionClosed(ConnectionClosed event) {
        errorMessageLabel.setText("Connection got closed");
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(NewUserEnteredRoom.class, this::newUserEnteredRoom)
                .toMessage(FailedToEnterRoom.class, this::failedToEnterRoom)
                .toMessage(StartingClientError.class, this::startingClientError)
                .toMessage(SendClientMessageError.class, this::sendClientMessageError)
                .toMessage(ConnectionClosed.class, this::connectionClosed)
                .toMessage(ConnectionEstablished.class, this::connectionEstablished)
                .subscriberName("online-client-join-game-gui");
    }
}