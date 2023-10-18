package com.noscompany.snake.game.online.user.registry.gui;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class UsersRegistryController extends AbstractController {
    @FXML
    private Label usersListLabel;

    public void newUserEnteredRoom(NewUserEnteredRoom event) {
        log.debug("new user entered the room, updating users list");
        Platform.runLater(() -> usersListLabel.setText(toString(event.getUsersInTheRoom())));
    }

    private void userLeftRoom(UserLeftRoom event) {
        log.debug("user left the room, updating users list");
        Platform.runLater(() -> usersListLabel.setText(toString(event.getUsersInTheRoom())));
    }

    private String toString(Collection<UserName> userNames) {
        return userNames
                .stream()
                .map(UserName::getName)
                .reduce("", (u1, u2) -> u1 + System.lineSeparator() + u2);
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(NewUserEnteredRoom.class, this::newUserEnteredRoom)
                .toMessage(UserLeftRoom.class, this::userLeftRoom)
                .subscriberName("joined-users-gui");
    }
}