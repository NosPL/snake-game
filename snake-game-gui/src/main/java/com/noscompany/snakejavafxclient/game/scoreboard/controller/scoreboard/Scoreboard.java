package com.noscompany.snakejavafxclient.game.scoreboard.controller.scoreboard;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import lombok.Value;
import snake.game.core.dto.Score;

import java.util.Collection;

public abstract class Scoreboard extends GridPane {

    public abstract void clear();

    public abstract void update(Collection<Entry> scoreEntries);

    @Value
    public static class Entry {
        int place;
        int score;
        Collection<Player> players;
    }

    @Value
    public static class Player {
        String name;
        Color color;
        boolean isAlive;

        public boolean isDead() {
            return !isAlive;
        }
    }
}