package com.noscompany.snakejavafxclient.components.commons.game.grid;

import javafx.scene.paint.Color;
import lombok.Value;
import com.noscompany.snake.game.online.contract.messages.game.dto.Position;

@Value
class PrintableFood {
    Position position;
    String sign;
    Color color;
}