package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import com.noscompany.snake.game.online.contract.messages.server.events.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerStarted;
import com.noscompany.snake.game.online.network.interfaces.analyzer.IpV4Address;
import com.noscompany.snake.game.online.network.interfaces.analyzer.NetworkInterfacesAnalyzerConfiguration;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class SetupHostController extends AbstractController {
    @FXML
    private Label messageForUserLabel;
    @FXML
    private ChoiceBox<String> ipAddressesChoiceBox;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField hostNameTextField;
    private MessagePublisherAdapter snakeOnlineHost;
    private AtomicBoolean isServerStarted;

    @FXML
    public void startHost() {
        if (!isServerStarted.get())
            startServer();
        else
            enterRoom();
    }

    private void startServer() {
        if (snakeOnlineHost == null)
            snakeOnlineHost = SnakeOnlineHostGuiConfiguration.createConfiguredHost();
        printMessage("starting server...");
        var serverParams = new ServerParams(ipAddressesChoiceBox.getValue(), portTextField.getText());
        snakeOnlineHost.startServer(serverParams);
    }

    public void handle(ServerStarted serverStarted) {
        isServerStarted.set(true);
        printMessage("server started, entering the room...");
        enterRoom();
    }

    private void enterRoom() {
        snakeOnlineHost.enterRoom(new UserName(hostNameTextField.getText()));
    }

    public void handle(FailedToStartServer event) {
        isServerStarted.set(false);
        printFailureMessage(toText(event.getReason()));
    }

    public void hostEnteredRoom() {
        printMessage("loading host screen...");
        Platform.runLater(() -> {
            SnakeOnlineHostStage.get().show();
            SetupHostStage.get().close();
            SetupHostStage.remove();
        });
    }

    public void handle(FailedToEnterRoom event) {
        printFailureMessage(toText(event.getReason()));
    }

    private void printMessage(String message) {
        Platform.runLater(() -> {
            messageForUserLabel.setTextFill(Color.BLACK);
            messageForUserLabel.setText(message);
        });
    }

    private void printFailureMessage(String message) {
        Platform.runLater(() -> {
            messageForUserLabel.setTextFill(Color.RED);
            messageForUserLabel.setText(message);
        });
    }
    
    private String toText(Enum<?> enumClass) {
        return enumClass
                .toString()
                .toLowerCase()
                .replace("_", " ");
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        super.doInitialize(location, resources);
        getAvailableIpV4Addresses().forEach(this::addToChoiceBox);
        isServerStarted = new AtomicBoolean(false);
    }

    private void addToChoiceBox(String string) {
        ipAddressesChoiceBox.getItems().add(string);
    }

    private List<String> getAvailableIpV4Addresses() {
        return new NetworkInterfacesAnalyzerConfiguration()
                .createNetworkInterfacesAnalyzer()
                .findIpV4Addresses().stream()
                .map(IpV4Address::getIp)
                .toList();
    }
}