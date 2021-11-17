package com.noscompany.snakejavafxclient.game.online;

import com.noscompany.snakejavafxclient.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class JoinedUsersController extends AbstractController {
    @FXML
    private TableView joinedUsersTableView;

    public void add(String userId) {
        joinedUsersTableView
                .getColumns()
                .add(new TableColumn<>(userId));
    }

    public void remove(String userId) {
        joinedUsersTableView
                .getColumns()
                .remove(new TableColumn<>(userId));
    }
}