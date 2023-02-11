package com.noscompany.snakejavafxclient.components.online.game.commons;

import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Set;

public class JoinedUsersController extends AbstractController {
    @FXML
    private Label usersListLabel;

    public void update(Set<String> users) {
        usersListLabel.setText(toString(users));
    }

    private String toString(Set<String> users) {
        return users.stream()
                .reduce("", (u1, u2) -> u1 + System.lineSeparator() + u2);
    }
}