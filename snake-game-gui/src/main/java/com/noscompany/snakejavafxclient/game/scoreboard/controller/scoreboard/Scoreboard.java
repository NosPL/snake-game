package com.noscompany.snakejavafxclient.game.scoreboard.controller.scoreboard;

import javafx.scene.layout.GridPane;
import snake.game.core.dto.Score;

public abstract class Scoreboard extends GridPane {

    public abstract void clear();

    public abstract void update(Score score);
}