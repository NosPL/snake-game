package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snake.game.online.client.HostAddress;
import com.noscompany.snake.game.online.client.SendClientMessageError;
import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.StartingClientError;
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
    private volatile Option<SnakeOnlineClient> snakeOnlineClientOption = Option.none();

    public void disconnect() {
        snakeOnlineClientOption.peek(SnakeOnlineClient::disconnect);
    }

    @FXML
    public void joinGame() {
        getIpAddress()
                .peek(this::joinGame)
                .peekLeft(this::handleError);
    }

    private void joinGame(HostAddress hostAddress) {
        if (!isClientConnected())
            getOrCreateSnakeOnlineClient().connect(hostAddress);
        else
            connectionEstablished();
    }

    public void connectionEstablished() {
        enterRoom();
    }

    public void enterRoom() {
        errorMessageLabel.setText("connection established, entering room...");
        var userNameString = playerNameTextField.getText();
        getOrCreateSnakeOnlineClient().enterTheRoom(new UserName(userNameString));
    }

    public void handle(NewUserEnteredRoom event) {
        if (nameOfNewlyConnectedUserEqualsNameTypedInTextField(event.getUserName())) {
            JoinGameStage.get().close();
            SnakeOnlineClientStage.get().show();
        }
    }

    public void handle(FailedToEnterRoom event) {
        String errorMessage = getErrorMessage(event);
        errorMessageLabel.setText(errorMessage);
        if (event.getReason() == FailedToEnterRoom.Reason.USER_ALREADY_IN_THE_ROOM) {
            JoinGameStage.get().close();
            SnakeOnlineClientStage.get().show();
        }
    }

    public void handle(SendClientMessageError sendClientMessageError) {
        errorMessageLabel.setText(toErrorMessage(sendClientMessageError));
    }

    public void handle(StartingClientError startingClientError) {
        errorMessageLabel.setText(toErrorMessage(startingClientError));
    }

    private SnakeOnlineClient getOrCreateSnakeOnlineClient() {
        if (snakeOnlineClientOption.isEmpty())
            snakeOnlineClientOption = Option.of(SnakeOnlineGuiClientConfiguration.createOnlineClient());
        return snakeOnlineClientOption.get();
    }

    private boolean isClientConnected() {
        return snakeOnlineClientOption
                .map(SnakeOnlineClient::isConnected)
                .getOrElse(false);
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

    private boolean nameOfNewlyConnectedUserEqualsNameTypedInTextField(UserName userName) {
        return userName.getName().equals(playerNameTextField.getText());
    }

    private String getErrorMessage(FailedToEnterRoom event) {
        return event.getReason().toString().toLowerCase().replace("_", " ");
    }

    public void connectionClosed() {
        snakeOnlineClientOption = Option.none();
        errorMessageLabel.setText("Connection got closed");
    }
}