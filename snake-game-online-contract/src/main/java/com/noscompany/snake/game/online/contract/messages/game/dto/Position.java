package com.noscompany.snake.game.online.contract.messages.game.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class Position {
    int x;
    int y;

    public Position getAdjacentPosition(Direction direction) {
        return move(direction, 1);
    }

    public Position move(Direction direction, int points) {
        if (direction == Direction.UP)
            return moveUpBy(points);
        else if (direction == Direction.DOWN)
            return moveDownBy(points);
        else if (direction == Direction.RIGHT)
            return moveRightBy(points);
        else if (direction == Direction.LEFT)
            return moveLeftBy(points);
        else
            throw new IllegalArgumentException("unknown direction: " + direction);
    }

    public static Position position(int x, int y) {
        return new Position(x, y);
    }

    public Position moveUpBy(int i) {
        return position(x, y + i);
    }

    public Position moveRightBy(int i) {
        return position(x + i, y);
    }

    public Position moveLeftBy(int i) {
        return position(x - i, y);
    }

    public Position moveDownBy(int i) {
        return position(x, y - i);
    }
}