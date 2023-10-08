package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.contract.messages.server.events.ServerFailedToSendMessageToRemoteClients;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import com.noscompany.snake.game.online.contract.messages.server.events.FailedToStartServer;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.BLACK;

public class ServerController extends AbstractController {
    private static final String STATUS_PREFIX = "Status: ";
    private static final String IP_ADDRESS_PREFIX = "Ip: ";
    private static final String PORT_PREFIX = "Port: ";

    @FXML private Label statusLabel;
    @FXML private Label ipAddressLabel;
    @FXML private Label portLabel;

    public void handle(FailedToStartServer serverFailedToStart) {
        statusLabel.setTextFill(Color.RED);
        statusLabel.setText(STATUS_PREFIX + toText(serverFailedToStart.getReason()));
    }

    public void serverStarted(ServerParams serverParams) {
        statusLabel.setTextFill(Color.GREEN);
        statusLabel.setText(STATUS_PREFIX + "Server started");
        ipAddressLabel.setText(IP_ADDRESS_PREFIX + serverParams.getIpAddress());
        portLabel.setText(PORT_PREFIX + serverParams.getPort());
    }

    public void handle(ServerFailedToSendMessageToRemoteClients event) {
        statusLabel.setTextFill(Color.ORANGE);
        statusLabel.setText(STATUS_PREFIX + toText(event.getReason()));
        ipAddressLabel.setText(IP_ADDRESS_PREFIX);
        portLabel.setText(PORT_PREFIX);
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        super.doInitialize(location, resources);
        statusLabel.setTextFill(BLACK);
    }

    private String toText(ServerFailedToSendMessageToRemoteClients.Reason cause) {
        return cause
                .toString()
                .toLowerCase()
                .replace("_", " ");
    }

    private String toText(FailedToStartServer.Reason reason) {
        return reason
                .toString()
                .toLowerCase()
                .replace("_", " ");
    }
}