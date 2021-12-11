package com.noscompany.snakejavafxclient.game;

import javafx.scene.paint.Color;
import snake.game.core.dto.SnakeNumber;

import java.util.Map;

public class SnakesColors {
    private static final Map<SnakeNumber, Color> snakesColors = Map.of(
            SnakeNumber._1, Color.RED,
            SnakeNumber._2, Color.BLUE,
            SnakeNumber._3, Color.GREEN,
            SnakeNumber._4, Color.GOLDENROD
    );

    public static Color get(SnakeNumber snakeNumber) {
        return snakesColors.get(snakeNumber);
    }
}