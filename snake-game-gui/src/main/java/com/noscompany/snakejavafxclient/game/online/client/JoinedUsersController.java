package com.noscompany.snakejavafxclient.game.online.client;

import com.noscompany.snakejavafxclient.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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