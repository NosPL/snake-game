package com.noscompany.snakejavafxclient;

import javafx.scene.paint.Color;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;

import java.util.Map;

public class SnakesColors {
    private static final Map<PlayerNumber, Color> snakesColors = Map.of(
            PlayerNumber._1, Color.RED,
            PlayerNumber._2, Color.BLUE,
            PlayerNumber._3, Color.GREEN,
            PlayerNumber._4, Color.DARKMAGENTA);

    public static Color get(PlayerNumber playerNumber) {
        return snakesColors.get(playerNumber);
    }
}