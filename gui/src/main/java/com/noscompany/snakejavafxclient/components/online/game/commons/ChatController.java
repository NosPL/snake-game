package com.noscompany.snakejavafxclient.components.online.game.commons;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
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

    @Override
    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(UserSentChatMessage.class, this::userSentChatMessage)
                .toMessage(FailedToSendChatMessage.class, this::failedToSendChatMessage)
                .subscriberName("chat-gui");
    }

    public ChatController onSendChatMessageButtonPress(Consumer<String> sendChatMessageAction) {
        this.sendChatMessageAction = sendChatMessageAction;
        return this;
    }

    private void failedToSendChatMessage(FailedToSendChatMessage event) {
        print(asString(event));
    }

    @FXML
    public void sendMessage() {
        var str = chatMessageTextField.getText();
        if (str.isBlank())
            return;
        sendChatMessageAction.accept(str);
        chatMessageTextField.setText("");
    }

    public void userSentChatMessage(UserSentChatMessage event) {
        print(asString(event));
    }

    private void print(String string) {
        if (messages.size() > 20) {
            messages.pollFirst();
        }
        messages.addLast(string);
        stringListView.setItems(FXCollections.observableArrayList(messages));
    }

    private String asString(UserSentChatMessage event) {
        return event.getUserName().getName() + ": " + event.getMessage();
    }

    private String asString(FailedToSendChatMessage event) {
        return event.getReason().toString().replace("_", " ");
    }
}