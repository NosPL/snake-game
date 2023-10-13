package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerFailedToSendMessageToRemoteClients;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import com.noscompany.snake.game.online.contract.messages.server.events.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerStarted;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
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

    @Override
    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(FailedToStartServer.class, this::failedToStartServer)
                .toMessage(ServerStarted.class, this::serverStarted)
                .subscriberName("host-server-gui");
    }

    public void failedToStartServer(FailedToStartServer serverFailedToStart) {
        statusLabel.setTextFill(Color.RED);
        statusLabel.setText(STATUS_PREFIX + toText(serverFailedToStart.getReason()));
    }

    public void serverStarted(ServerStarted event) {
        statusLabel.setTextFill(Color.GREEN);
        statusLabel.setText(STATUS_PREFIX + "Server started");
        ipAddressLabel.setText(IP_ADDRESS_PREFIX + event.getServerParams().getIpAddress());
        portLabel.setText(PORT_PREFIX + event.getServerParams().getPort());
    }

    public void failedToSendMessage(ServerFailedToSendMessageToRemoteClients event) {
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