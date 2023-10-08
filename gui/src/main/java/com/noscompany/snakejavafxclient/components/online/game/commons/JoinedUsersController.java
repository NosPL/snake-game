package com.noscompany.snakejavafxclient.components.online.game.commons;

import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Collection;
import java.util.Set;

public class JoinedUsersController extends AbstractController {
    @FXML
    private Label usersListLabel;

    public void update(Collection<UserName> users) {
        usersListLabel.setText(toString(users));
    }

    private String toString(Collection<UserName> userNames) {
        return userNames
                .stream()
                .map(UserName::getName)
                .reduce("", (u1, u2) -> u1 + System.lineSeparator() + u2);
    }
}