package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import com.noscompany.snake.game.online.contract.messages.server.events.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerStarted;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snake.game.online.network.interfaces.analyzer.IpV4Address;
import com.noscompany.snake.game.online.network.interfaces.analyzer.NetworkInterfacesAnalyzerConfiguration;
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
import java.util.function.Consumer;

public class SetupHostController extends AbstractController {
    @FXML
    private Label messageForUserLabel;
    @FXML
    private ChoiceBox<String> ipAddressesChoiceBox;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField hostNameTextField;
    private AtomicBoolean isServerStarted;
    private Consumer<ServerParams> startServer = sp -> {};
    private Consumer<UserName> enterRoom = un -> {};

    public void configure(Consumer<ServerParams> startServer, Consumer<UserName> enterRoom) {
        this.startServer = startServer;
        this.enterRoom = enterRoom;
    }

    @FXML
    public void startHost() {
        if (!isServerStarted.get()) {
            startServer();
        } else {
            enterRoom.accept(new UserName(hostNameTextField.getText()));
        }
    }

    private void startServer() {
        printMessage("starting server...");
        getServerParams();
        startServer.accept(getServerParams());
    }

    private ServerParams getServerParams() {
        return new ServerParams(ipAddressesChoiceBox.getValue(), portTextField.getText());
    }

    public void serverStarted(ServerStarted event) {
        isServerStarted.set(true);
        printMessage("server started, entering the room...");
        enterRoom.accept(new UserName(hostNameTextField.getText()));
    }

    private void failedToStartServer(FailedToStartServer event) {
        isServerStarted.set(false);
        printFailureMessage(toText(event.getReason()));
    }

    private void newUserEnteredRoom(NewUserEnteredRoom event) {
        hostEnteredRoom();
    }

    private void hostEnteredRoom() {
        printMessage("loading host screen...");
        Platform.runLater(() -> {
            SnakeOnlineHostStage.get().show();
            SetupHostStage.get().close();
            SetupHostStage.remove();
        });
    }

    private void failedToEnterRoom(FailedToEnterRoom event) {
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
        ipAddressesChoiceBox.getSelectionModel().selectFirst();
        isServerStarted = new AtomicBoolean(false);
    }

    private void addToChoiceBox(String string) {
        ipAddressesChoiceBox.getItems().add(string);
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(NewUserEnteredRoom.class, this::newUserEnteredRoom)
                .toMessage(FailedToEnterRoom.class, this::failedToEnterRoom)
                .toMessage(FailedToStartServer.class, this::failedToStartServer)
                .toMessage(ServerStarted.class, this::serverStarted)
                .subscriberName("setup-host-gui");
    }

    private List<String> getAvailableIpV4Addresses() {
        return new NetworkInterfacesAnalyzerConfiguration()
                .createNetworkInterfacesAnalyzer()
                .findIpV4Addresses().stream()
                .map(IpV4Address::getIp)
                .toList();
    }
}