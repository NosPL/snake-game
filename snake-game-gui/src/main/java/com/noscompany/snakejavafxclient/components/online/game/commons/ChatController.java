package com.noscompany.snakejavafxclient.components.online.game.commons;

import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ChatController extends AbstractController {
    @FXML
    private ListView<String> stringListView;
    private LinkedList<String> messages;
    @FXML
    private TextField chatMessageTextField;
    private Consumer<String> sendChatMessageAction = str -> {
    };

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        messages = new LinkedList<>();
        chatMessageTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER))
                sendMessage();
        });
    }

    public ChatController onSendChatMessageButtonPress(Consumer<String> sendChatMessageAction) {
        this.sendChatMessageAction = sendChatMessageAction;
        return this;
    }

    @FXML
    public void sendMessage() {
        var str = chatMessageTextField.getText();
        if (str.isBlank())
            return;
        sendChatMessageAction.accept(str);
        chatMessageTextField.setText("");
    }

    public void print(UserSentChatMessage event) {
        if (messages.size() > 20) {
            messages.pollFirst();
        }
        messages.addLast(toString(event));
        stringListView.setItems(FXCollections.observableArrayList(messages));
    }

    private String toString(UserSentChatMessage event) {
        return event.getUserName() + ": " + event.getMessage();
    }
}