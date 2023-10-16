package com.noscompany.snake.game.online.gameplay.gui.grid;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;
import javafx.scene.paint.Color;
import lombok.Value;

import java.util.List;

@Value
class PrintableSnake {
    Node headNode;
    List<Node> bodyNodes;

    @Value
    static class Node {
        Position position;
        String string;
        Color color;
    }
}