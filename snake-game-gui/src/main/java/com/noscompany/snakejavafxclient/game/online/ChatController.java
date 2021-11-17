package com.noscompany.snakejavafxclient.game.online;

import com.noscompany.snakejavafxclient.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class ChatController extends AbstractController {
    @FXML
    private TableView chatTableView;
    @FXML
    private TextField chatMessageTextField;
    private Consumer<String> sendChatMessageAction = str -> {
    };

    @FXML
    public void sendMessage() {
        var str = chatMessageTextField.getText();
        if (str.isBlank())
            return;
        sendChatMessageAction.accept(str);
        chatMessageTextField.setText("");
    }

    public void messageReceived(String chatMessageAuthor, String chatMessageContent) {
        chatTableView
                .getColumns()
                .add(new TableColumn<>(chatMessageAuthor + ": " + chatMessageContent));
    }

    public ChatController onSendChatMessage(Consumer<String> sendChatMessageAction) {
        this.sendChatMessageAction = sendChatMessageAction;
        return this;
    }
}