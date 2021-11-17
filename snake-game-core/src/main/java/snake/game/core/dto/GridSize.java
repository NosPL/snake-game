package snake.game.core.dto;

import lombok.*;

import java.util.List;

import static io.vavr.API.For;
import static io.vavr.collection.Vector.range;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor
@Getter
@NoArgsConstructor(force = true, access = PRIVATE)
public enum GridSize {

    _10x10(10, 10),
    _15x15(15, 15),
    _20x20(20, 20),
    _25x25(25, 25);

    private int width;
    private int height;

    public List<Point> allPoints() {
        return For(range(0, width), range(0, height))
                .yield(Point::point)
                .toJavaList();
    }

    public boolean contains(Point point) {
        return (point.getX() < width && point.getX() >= 0) &&
                (point.getY() < height && point.getY() >= 0);
    }

    public Point bottomRightCorner() {
        return Point.point(width - 1, 0);
    }

    public Point bottomLeftCorner() {
        return Point.point(0, 0);
    }

    public Point topRightCorner() {
        return Point.point(width - 1, height - 1);
    }

    public Point topLeftCorner() {
        return Point.point(0, height - 1);
    }
}