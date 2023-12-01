package com.noscompany.snake.game.online.chat.gui;

import chat.messages.FailedToSendChatMessage;
import chat.messages.UserSentChatMessage;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Slf4j
public class ChatController extends AbstractController {
    @FXML
    private ListView<String> stringListView;
    private LinkedList<String> messages = new LinkedList<>();
    @FXML
    private TextField chatMessageTextField;
    private Consumer<String> sendChatMessageAction = str -> {
    };

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        super.doInitialize(location, resources);
        chatMessageTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER))
                sendMessage();
        });
    }

    @FXML
    public void sendMessage() {
        var message = chatMessageTextField.getText();
        if (message.isBlank())
            return;
        log.debug("sending chat message: {}", message);
        sendChatMessageAction.accept(message);
        chatMessageTextField.setText("");
    }

    public void printChatMessage(UserName messageAuthor, String messageContent) {
        var chatEntry = messageAuthor.getName() + ": " + messageContent;
        log.debug("printing chat message {}", chatEntry);
        addText(chatEntry);
    }

    public void printFailureMessage(Enum<?> e) {
        var string = e.toString().replace("_", " ");
        log.debug("printing failure message: {}", string);
        addText(string);
    }

    public void addText(String text) {
        if (messages.size() > 20) {
            messages.pollFirst();
        }
        messages.addLast(text);
        Platform.runLater(() -> stringListView.setItems(FXCollections.observableArrayList(messages)));
    }

    public ChatController onSendChatMessageButtonPress(Consumer<String> sendChatMessageAction) {
        this.sendChatMessageAction = sendChatMessageAction;
        return this;
    }

    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(UserSentChatMessage.class, (UserSentChatMessage e) -> printChatMessage(e.getUserName(), e.getMessage()))
                .toMessage(FailedToSendChatMessage.class, (FailedToSendChatMessage event) -> printFailureMessage(event.getReason()))
                .subscriberName("chat-gui");
    }
}