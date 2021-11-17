package snake.game.core.logic.snakes;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import snake.game.core.dto.Direction;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.logic.food.Food;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.max;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PACKAGE)
class Snake {
    @Getter
    private final SnakeNumber snakeNumber;
    private final LinkedList<Node> nodes;
    private final GridTeleport gridTeleport;
    private Direction currentDirection;
    private boolean killed;

    void kill() {
        killed = true;
    }

    boolean isKilled() {
        return killed;
    }

    boolean isAlive() {
        return !isKilled();
    }

    Option<SnakeConsumedFood> moveAndConsume(Food food) {
        nodes
                .getLast()
                .createNewNodeFromFood()
                .peek(nodes::addLast);
        nodes.removeLast();
        var newHead = newHead();
        nodes.addFirst(newHead);
        var foodConsumed = newHead.consume(food);
        if (foodConsumed)
            return Option.of(new SnakeConsumedFood(snakeNumber));
        else
            return Option.none();
    }

    private Node newHead() {
        var newHeadPoint = calculateNewHeadPoint(currentDirection);
        return new Node(newHeadPoint, false);
    }

    void changeDirectionTo(Direction newDirection) {
        if (headWillHitFollowingNodeByApplying(newDirection))
            return;
        this.currentDirection = newDirection;
    }

    private boolean headWillHitFollowingNodeByApplying(Direction direction) {
        return calculateNewHeadPoint(direction)
                .equals(getNodeAfterHead().getPoint());
    }

    private Point calculateNewHeadPoint(Direction direction) {
        var newHeadPoint = nodes
                .getFirst()
                .getPoint()
                .getAdjacentPoint(direction);
        return gridTeleport
                .teleportPointIfItWentOutsideGrid(newHeadPoint);
    }

    private Node getNodeAfterHead() {
        return nodes.get(1);
    }

    private List<Node> getBodyNodes() {
        var bodyNodes = new LinkedList<>(nodes);
        bodyNodes.removeFirst();
        return bodyNodes;
    }

    public SnakeDto toDto() {
        return DtoMapper.toDto(this);
    }

    private static class DtoMapper {

        private static SnakeDto toDto(Snake snake) {
            return new SnakeDto(
                    snake.snakeNumber,
                    snake.isAlive(),
                    headOf(snake),
                    bodyOf(snake));
        }

        private static SnakeDto.Head headOf(Snake s) {
            var head = s.nodes.getFirst();
            return new SnakeDto.Head(
                    s.snakeNumber,
                    head.getPoint(),
                    s.currentDirection,
                    head.hasFood());
        }

        private static SnakeDto.Body bodyOf(Snake s) {
            var bodyNodes = s
                    .getBodyNodes()
                    .stream()
                    .map(DtoMapper::toBodyPart)
                    .collect(toList());
            return new SnakeDto.Body(s.snakeNumber, bodyNodes);
        }

        private static SnakeDto.Body.Part toBodyPart(Node node) {
            return new SnakeDto.Body.Part(node.getPoint(), node.hasFood());
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    static class Creator {
        private int snakeLength = 4;
        private SnakeNumber snakeNumber;
        private Point headPoint;
        private Direction headDirection;
        private Direction bodyDirectionFromHead;
        private GridTeleport gridTeleport = new GridTeleport.TeleportDisabled();

        public static Creator playerNumber(SnakeNumber snakeNumber) {
            final Creator creator = new Creator();
            creator.snakeNumber = snakeNumber;
            return creator;
        }

        private static LinkedList<Node> createNodes(int length, Point startingPoint, Direction directionFromStartingPoint) {
            return IntStream
                    .range(0, max(length, 2))
                    .mapToObj(i -> startingPoint.move(directionFromStartingPoint, i))
                    .map(point -> new Node(point, false))
                    .collect(toCollection(LinkedList::new));
        }

        public Creator length(int snakeLength) {
            this.snakeLength = snakeLength;
            return this;
        }

        public Creator head(Head head) {
            this.headPoint = head.point;
            this.headDirection = head.direction;
            return this;
        }

        public Creator restOfBodyFromHead(Direction bodyDirection) {
            this.bodyDirectionFromHead = bodyDirection;
            return this;
        }

        public Creator wallTeleport(GridTeleport teleport) {
            this.gridTeleport = teleport;
            return this;
        }

        public Snake create() {
            if (bodyDirectionFromHead == headDirection)
                headDirection = headDirection.getOpposite();
            var nodes = createNodes(snakeLength, headPoint, bodyDirectionFromHead);
            return new Snake(snakeNumber, nodes, gridTeleport, headDirection, false);
        }

        @RequiredArgsConstructor(access = PRIVATE)
        public static class Head {
            private final Point point;
            private Direction direction;

            public static Head at(Point point) {
                return new Head(point);
            }

            public Head inDirection(Direction direction) {
                this.direction = direction;
                return this;
            }
        }
    }
}