package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.server.dto.ServerStartError;
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

    public void handle(ServerStartError serverStartError) {
        statusLabel.setTextFill(Color.RED);
        statusLabel.setText(STATUS_PREFIX + serverStartError.getCause().getMessage());
    }

    public void serverStarted(ServerParams serverParams) {
        statusLabel.setTextFill(Color.GREEN);
        statusLabel.setText(STATUS_PREFIX + "Server started");
        ipAddressLabel.setText(IP_ADDRESS_PREFIX + serverParams.getHost());
        portLabel.setText(PORT_PREFIX + serverParams.getPort());
    }

    public void failedToExecuteActionBecauseServerIsNotRunning() {
        statusLabel.setTextFill(Color.RED);
        statusLabel.setText(STATUS_PREFIX + "Failed to start server!");
        ipAddressLabel.setText(IP_ADDRESS_PREFIX);
        portLabel.setText(PORT_PREFIX);
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        super.doInitialize(location, resources);
        statusLabel.setTextFill(BLACK);
    }
}