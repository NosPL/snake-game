package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.contract.messages.room.PlayerName;
import com.noscompany.snake.game.online.contract.messages.room.PlayersLimit;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.network.interfaces.analyzer.IpV4Address;
import com.noscompany.snake.game.online.network.interfaces.analyzer.NetworkInterfacesAnalyzerConfiguration;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import io.vavr.control.Option;
import io.vavr.control.Try;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SetupHostController extends AbstractController {
    @FXML
    private Label errorMessageLabel;
    @FXML
    private ChoiceBox<String> ipAddressesChoiceBox;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField playerNameTextField;

    @FXML
    public void startServer() {
        errorMessageLabel.setText("");
        getServerParams()
                .peek(this::startServer);
    }

    private void startServer(ServerParams serverParams) {
        getPlayerName()
                .peek(playerName -> startServer(serverParams, playerName));
    }

    private void startServer(ServerParams serverParams, PlayerName playerName) {
        getPlayerLimit()
                .peek(playersLimit -> startServer(serverParams, playerName, playersLimit));
    }

    private void startServer(ServerParams serverParams, PlayerName playerName, PlayersLimit playersLimit) {
        SnakeOnlineHostGuiConfiguration
                .createConfiguredHost(playersLimit)
                .startServer(serverParams, playerName);
    }

    private Option<PlayersLimit> getPlayerLimit() {
        return Option.of(new PlayersLimit(10));
    }

    private Option<ServerParams> getServerParams() {
        String ipAddress = ipAddressesChoiceBox.getSelectionModel().getSelectedItem();
        if (ipAddress == null) {
            errorMessageLabel.setText("Ip address must be set");
            return Option.none();
        }
        return getServerParams(ipAddress);
    }

    private Option<ServerParams> getServerParams(String ipAddress) {
        String portStr = portTextField.getText();
        if (portStr == null || portStr.isBlank()) {
            errorMessageLabel.setText("Port must be set");
            return Option.none();
        }
        var parsedPortValue = Try.of(() -> Integer.parseInt(portStr));
        if (parsedPortValue.isFailure()) {
            errorMessageLabel.setText("Port value must be numerical value");
            return Option.none();
        }
        if (parsedPortValue.get() < 0) {
            errorMessageLabel.setText("Port value must be > 0");
            return Option.none();
        }
        return Option.of(new ServerParams(ipAddress, parsedPortValue.get()));
    }

    private Option<PlayerName> getPlayerName() {
        String playerNameStr = playerNameTextField.getText();
        if (playerNameStr == null || playerNameStr.isBlank()) {
            errorMessageLabel.setText("Player name cannot be empty");
            return Option.none();
        }
        if (playerNameStr.length() > 10) {
            errorMessageLabel.setText("Player name cannot be longer than 10 signs");
            return Option.none();
        }
        return Option.of(new PlayerName(playerNameStr));
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        getAvailableIpV4Addresses().forEach(this::addToChoiceBox);
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