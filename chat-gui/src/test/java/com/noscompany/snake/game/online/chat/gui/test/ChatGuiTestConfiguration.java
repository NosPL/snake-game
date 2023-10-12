package com.noscompany.snake.game.online.chat.gui.test;

import com.noscompany.snake.game.online.chat.gui.ChatController;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.gui.commons.Controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ChatGuiTestConfiguration {

    @SneakyThrows
    public Parent create() {
        var chat = FXMLLoader.load(getClass().getResource("/chat/chat-view.fxml"));
        var chatController = Controllers.get(ChatController.class);
        chatController.onSendChatMessageButtonPress(str -> chatController.printChatMessage(new UserName("Test Name"), str));
        return (Parent) chat;
    }
}