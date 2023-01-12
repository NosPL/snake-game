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
        int port;
        String ipAddress;
        try {
            port = Integer.parseInt(portTextField.getText());
            if (port < 0)
                throw new RuntimeException();
        } catch (Exception e) {
            errorMessageLabel.setText("Wrong port value");
            return;
        }
        try {
            ipAddress = ipAddressesChoiceBox.getSelectionModel().getSelectedItem();
            if (ipAddress == null)
                throw new RuntimeException();
        } catch (Exception e) {
            errorMessageLabel.setText("Ip Address not chosen");
            return;
        }
        String playerName = playerNameTextField.getText();
        if (playerName == null || playerName.isBlank()) {
            errorMessageLabel.setText("Player name cannot be empty");
            return;
        }
        if (playerName.length() > 10) {
            errorMessageLabel.setText("Player name cannot be longer than 10 signs");
            return;
        }
        SnakeOnlineHostGuiConfiguration.run(new ServerParams(ipAddress, port), new PlayerName(playerName));
        SetupHostStage.get().close();
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        getIpAddresses().forEach(this::addToChoiceBox);
    }

    private void addToChoiceBox(String string) {
        ipAddressesChoiceBox.getItems().add(string);
    }

    private List<String> getIpAddresses() {
        return ipFetcher
                .fetch().stream()
                .map(IpAddress::getIp)
                .toList();
    }
}