package snake.game.core.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class Point {
    int x;
    int y;

    public Point getAdjacentPoint(Direction direction) {
        return move(direction, 1);
    }

    public Point move(Direction direction, int points) {
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

    public static Point point(int x, int y) {
        return new Point(x, y);
    }

    public Point moveUpBy(int i) {
        return point(x, y + i);
    }

    public Point moveRightBy(int i) {
        return point(x + i, y);
    }

    public Point moveLeftBy(int i) {
        return point(x - i, y);
    }

    public Point moveDownBy(int i) {
        return point(x, y - i);
    }
}