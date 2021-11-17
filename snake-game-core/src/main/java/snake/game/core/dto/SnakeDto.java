package snake.game.core.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class SnakeDto {
    SnakeNumber snakeNumber;
    boolean alive;
    Head head;
    Body body;

    public boolean gotHitBy(SnakeDto snake) {
        return this.getBody().gotHitBy(snake.getHead());
    }

    public boolean hitItself() {
        return gotHitBy(this);
    }

    public boolean wentOutside(GridSize gridSize) {
        return !gridSize.contains(head.getPoint());
    }

    public List<Point> allPoints() {
        var points = new LinkedList<Point>();
        points.add(head.point);
        points.addAll(body.allPoints());
        return points;
    }

    @Value
    @NoArgsConstructor(force = true, access = PRIVATE)
    @AllArgsConstructor
    public static class Head {
        SnakeNumber snakeNumber;
        Point point;
        Direction direction;
        boolean withFood;
    }

    @Value
    @NoArgsConstructor(force = true, access = PRIVATE)
    @AllArgsConstructor
    public static class Body {
        SnakeNumber snakeNumber;
        List<Part> parts;

        public boolean gotHitBy(Head head) {
            var headPoint = head.getPoint();
            return parts
                    .stream()
                    .map(Part::getPoint)
                    .anyMatch(bodyPoint -> bodyPoint.equals(headPoint));
        }

        public boolean contains(Point point) {
            return allPoints().contains(point);
        }

        private List<Point> allPoints() {
            return parts
                    .stream()
                    .map(Part::getPoint)
                    .collect(toList());
        }

        @Value
        @NoArgsConstructor(force = true, access = PRIVATE)
        @AllArgsConstructor
        public static class Part {
            Point point;
            boolean withFood;
        }
    }
}