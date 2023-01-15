package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.host.AvailableHostIpv4AddressesFetcher;
import com.noscompany.snake.game.online.host.room.mediator.PlayerName;
import com.noscompany.snake.game.online.host.server.dto.IpAddress;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private final AvailableHostIpv4AddressesFetcher ipFetcher = new AvailableHostIpv4AddressesFetcher();

    @FXML
    public void startServer() {
        try {
            ServerParams serverParams = getServerParams();
            PlayerName playerName = getPlayerName();
            SnakeOnlineHostGuiConfiguration
                    .createConfiguredHost()
                    .startServer(serverParams, playerName);
        } catch (Exception e) {
        }
    }

    private ServerParams getServerParams() {
        String ipAddress = ipAddressesChoiceBox.getSelectionModel().getSelectedItem();
        if (ipAddress == null) {
            errorMessageLabel.setText("Ip Address not chosen");
            throw new RuntimeException();
        }
        int port = Integer.parseInt(portTextField.getText());
        if (port < 0) {
            errorMessageLabel.setText("Wrong port value");
            throw new RuntimeException();
        }
        return new ServerParams(ipAddress, port);
    }

    private PlayerName getPlayerName() {
        String playerNameStr = playerNameTextField.getText();
        if (playerNameStr == null || playerNameStr.isBlank()) {
            errorMessageLabel.setText("Player name cannot be empty");
            throw new RuntimeException();
        }
        if (playerNameStr.length() > 10) {
            errorMessageLabel.setText("Player name cannot be longer than 10 signs");
            throw new RuntimeException();
        }
        return new PlayerName(playerNameStr);
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        getNetworkInterfacesIpAddresses().forEach(this::addToChoiceBox);
    }

    private void addToChoiceBox(String string) {
        ipAddressesChoiceBox.getItems().add(string);
    }

    private List<String> getNetworkInterfacesIpAddresses() {
        return ipFetcher
                .fetch().stream()
                .map(IpAddress::getIp)
                .toList();
    }
}