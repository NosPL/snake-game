package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.client.*;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import io.vavr.control.Either;
import io.vavr.control.Option;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class JoinGameController extends AbstractController {
    @FXML
    private TextField ip1TextField;
    @FXML
    private TextField ip2TextField;
    @FXML
    private TextField ip3TextField;
    @FXML
    private TextField ip4TextField;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField playerNameTextField;
    @FXML
    private Label errorMessageLabel;
    private final HostAddressCreator hostAddressCreator = new HostAddressCreator();
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
        String errorMessage = getErrorMessage(event);
        errorMessageLabel.setText(errorMessage);
        if (event.getReason() == FailedToEnterRoom.Reason.USER_ALREADY_IN_THE_ROOM) {
            JoinGameStage.get().close();
            SnakeOnlineClientStage.get().show();
        }
    }

    public void sendClientMessageError(SendClientMessageError sendClientMessageError) {
        errorMessageLabel.setText(toErrorMessage(sendClientMessageError));
    }

    public void startingClientError(StartingClientError startingClientError) {
        errorMessageLabel.setText(toErrorMessage(startingClientError));
    }

    private Either<HostAddressCreator.Error, HostAddress> getIpAddress() {
        return hostAddressCreator
                .create(
                        ip1TextField.getText(),
                        ip2TextField.getText(),
                        ip3TextField.getText(),
                        ip4TextField.getText(),
                        portTextField.getText());
    }

    private String toErrorMessage(StartingClientError startingClientError) {
        return startingClientError
                .toString()
                .replace("_", " ")
                .toLowerCase();
    }

    private String toErrorMessage(SendClientMessageError sendClientMessageError) {
        return sendClientMessageError
                .toString()
                .replace("_", " ")
                .toLowerCase();
    }

    private void handleError(HostAddressCreator.Error error) {
        String message = error
                .toString()
                .replace("_", " ")
                .toLowerCase();
        errorMessageLabel.setText(message);
    }

    private String getErrorMessage(FailedToEnterRoom event) {
        return event.getReason().toString().toLowerCase().replace("_", " ");
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