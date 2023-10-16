package com.noscompany.snakejavafxclient.components.local.game.grid;

import com.noscompany.snake.game.online.gui.commons.SnakesColors;
import javafx.scene.paint.Color;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction.*;
import static java.util.stream.Collectors.toList;

class SnakeMapper {

    List<PrintableSnake> map(Collection<Snake> snakes) {
        return snakes
                .stream()
                .map(this::map)
                .collect(toList());
    }

    PrintableSnake map(Snake snake) {
        PrintableSnake.Node head = mapHeadOf(snake);
        List<PrintableSnake.Node> body = mapBodyOf(snake);
        return new PrintableSnake(head, body);
    }

    private PrintableSnake.Node mapHeadOf(Snake snake) {
        String sign = headSignOf(snake);
        Position position = snake.getHeadNode().getPosition();
        Color color = getColorFor(snake);
        return new PrintableSnake.Node(position, sign, color);
    }

    private String headSignOf(Snake snake) {
        if (!snake.isAlive())
            return GameGridSigns.DEAD_HEAD;
        else if (snake.getHeadNode().isFed())
            return GameGridSigns.HEAD_WITH_FOOD;
        else {
            Direction direction = snake.getDirection();
            if (direction == UP)
                return GameGridSigns.HEAD_DOWN;
            else if (direction == DOWN)
                return GameGridSigns.HEAD_UP;
            else if (direction == LEFT)
                return GameGridSigns.HEAD_LEFT;
            else
                return GameGridSigns.HEAD_RIGHT;
        }
    }

    private List<PrintableSnake.Node> mapBodyOf(Snake snake) {
        return snake
                .getBodyNodes()
                .stream()
                .map(node -> toStringNode(snake, node))
                .collect(toList());
    }

    private PrintableSnake.Node toStringNode(Snake snake, Snake.Node node) {
        String sign = bodyPartSign(snake, node);
        Position position = node.getPosition();
        Color color = getColorFor(snake);
        return new PrintableSnake.Node(position, sign, color);
    }

    private String bodyPartSign(Snake snake, Snake.Node node) {
        if (!snake.isAlive())
            return GameGridSigns.DEAD_BODY;
        else if (node.isFed())
            return GameGridSigns.ALIVE_BODY_WITH_FOOD;
        else
            return GameGridSigns.ALIVE_BODY;
    }

    private Color getColorFor(Snake snake) {
        Color color = SnakesColors.get(snake.getPlayerNumber());
        return Objects.requireNonNull(color);
    }
}