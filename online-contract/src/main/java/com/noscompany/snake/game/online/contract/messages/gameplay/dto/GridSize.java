package com.noscompany.snake.game.online.contract.messages.gameplay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static io.vavr.API.For;
import static io.vavr.collection.Vector.range;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor
@Getter
@NoArgsConstructor(force = true, access = PRIVATE)
public enum GridSize {
    _5X5(5, 5),
    _10x10(10, 10),
    _15x15(15, 15),
    _20x20(20, 20),
    _25x25(25, 25);

    private int width;
    private int height;

    public List<Position> allPositions() {
        return For(range(0, width), range(0, height))
                .yield(Position::position)
                .toJavaList();
    }

    public boolean contains(Position position) {
        return (position.getX() < width && position.getX() >= 0) &&
                (position.getY() < height && position.getY() >= 0);
    }

    public Position bottomRightCorner() {
        return Position.position(width - 1, 0);
    }

    public Position bottomLeftCorner() {
        return Position.position(0, 0);
    }

    public Position topRightCorner() {
        return Position.position(width - 1, height - 1);
    }

    public Position topLeftCorner() {
        return Position.position(0, height - 1);
    }
}