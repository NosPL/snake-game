package com.noscompany.snake.game.online.remote.client.gui;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.client.*;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.network.YourIdGotInitialized;
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
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.RED;

public class JoinGameController extends AbstractController {
    @FXML
    private TextField ipTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField playerNameTextField;
    @FXML
    private Label messageLabel;
    private SnakeOnlineClient snakeOnlineClient;
    private Runnable onCloseClientAction = () -> {
    };

    public void setOnCloseAction(Runnable onCloseAction) {
        this.onCloseClientAction = onCloseAction;
    }

    public void setSnakeOnlineClient(SnakeOnlineClient snakeOnlineClient) {
        this.snakeOnlineClient = snakeOnlineClient;
    }

    @FXML
    public void joinGame() {
        getIpAddress()
                .peek(this::joinGame)
                .peekLeft(this::printError);
    }

    private void joinGame(HostAddress hostAddress) {
        if (!snakeOnlineClient.isConnected()) {
            printMessage("connecting to the host...");
            snakeOnlineClient.connect(hostAddress);
        } else
            enterRoom();
    }

    public void connectionEstablished(ConnectionEstablished event) {
        Platform.runLater(() -> printMessage("connection established, initializing user id..."));
    }

    private void yourIdGotInitialized(YourIdGotInitialized event) {
        Platform.runLater(() -> {
            printMessage("user id got initialized, entering room...");
            enterRoom();
        });
    }

    public void enterRoom() {
        Platform.runLater(() -> printMessage("entering room..."));
        var userNameString = playerNameTextField.getText();
        snakeOnlineClient.enterTheRoom(new UserName(userNameString));
    }

    public void newUserEnteredRoom(NewUserEnteredRoom event) {
        if (itsYourId(event.getUserId())) {
            Platform.runLater(() -> {
                JoinGameStage.get().close();
                SnakeOnlineClientStage.get().show();
                SnakeOnlineClientStage
                        .get()
                        .setOnCloseRequest(e -> {
                            snakeOnlineClient.disconnect();
                            onCloseClientAction.run();
                        });
            });
        }
    }

    private Boolean itsYourId(UserId userId) {
        return RemoteClientIdHolder
                .userId()
                .map(userId::equals)
                .getOrElse(false);
    }

    public void failedToEnterRoom(FailedToEnterRoom event) {
        printError(event.getReason());
        if (event.getReason() == USER_ALREADY_IN_THE_ROOM) {
            JoinGameStage.get().close();
            SnakeOnlineClientStage.get().show();
        }
    }

    public void sendClientMessageError(SendClientMessageError error) {
        printError(error);
    }

    public void startingClientError(StartingClientError error) {
        printError(error);
    }

    public void connectionClosed(ConnectionClosed event) {
        Platform.runLater(() -> printMessage("Connection got closed"));
    }

    private Either<StartingClientError, HostAddress> getIpAddress() {
        var portString = portTextField.getText();
        if (!isNumber(portString))
            return Either.left(PORT_IS_NOT_A_NUMBER);
        int port = Integer.parseInt(portString);
        var ipString = ipTextField.getText();
        return Either.right(new HostAddress(ipString, port));
    }

    private boolean isNumber(String portString) {
        return Try.of(() -> Integer.parseInt(portString)).isSuccess();
    }

    private void printMessage(String message) {
        messageLabel.setTextFill(BLACK);
        messageLabel.setText(message);
    }

    private void printError(Enum<?> error) {
        Platform.runLater(() -> {
            messageLabel.setTextFill(RED);
            messageLabel.setText(asString(error));
        });
    }

    private String asString(Enum<?> anEnum) {
        return anEnum.toString().toLowerCase().replace("_", " ");
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(YourIdGotInitialized.class, this::yourIdGotInitialized)
                .toMessage(NewUserEnteredRoom.class, this::newUserEnteredRoom)
                .toMessage(FailedToEnterRoom.class, this::failedToEnterRoom)
                .toMessage(StartingClientError.class, this::startingClientError)
                .toMessage(SendClientMessageError.class, this::sendClientMessageError)
                .toMessage(ConnectionClosed.class, this::connectionClosed)
                .toMessage(ConnectionEstablished.class, this::connectionEstablished)
                .subscriberName("online-client-join-game-gui");
    }
}