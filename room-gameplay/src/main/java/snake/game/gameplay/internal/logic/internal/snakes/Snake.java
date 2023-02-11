package snake.game.gameplay.internal.logic.internal.snakes;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction.*;
import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PACKAGE)
class Snake {
    @Getter
    private final PlayerNumber playerNumber;
    private final LinkedList<Node> nodes;
    private final GridTeleport gridTeleport;
    private Direction currentDirection;
    private AtomicBoolean isKilled;

    void kill() {
        isKilled.set(true);
    }

    boolean isAlive() {
        return !isKilled.get();
    }

    void move() {
        if (isKilled.get())
            return;
        Node newHead = createNewHead();
        nodes.addFirst(newHead);
        nodes.getLast()
                .tryToGrow()
                .peek(nodes::addLast);
        nodes.removeLast();
    }

    boolean tryToEatFood(Position foodPosition) {
        if (isKilled.get())
            return false;
        return getHead().tryToEatFood(foodPosition);
    }

    private Node getHead() {
        return nodes.getFirst();
    }

    private Node createNewHead() {
        var newHeadPosition = calculateNewHeadPosition(currentDirection);
        return new Node(newHeadPosition, false);
    }

    boolean changeDirection(Direction newDirection) {
        if (isKilled.get())
            return false;
        if (headWillHitFollowingNodeByApplying(newDirection))
            return false;
        this.currentDirection = newDirection;
        return true;
    }

    private boolean headWillHitFollowingNodeByApplying(Direction direction) {
        Position newHeadPosition = calculateNewHeadPosition(direction);
        Position nodeAfterHeadPosition = getNodeAfterHead().getPosition();
        return newHeadPosition.equals(nodeAfterHeadPosition);
    }

    private Position calculateNewHeadPosition(Direction direction) {
        var newHeadPosition = getHeadPosition().getAdjacentPosition(direction);
        return gridTeleport.teleport(newHeadPosition);
    }

    private Node getNodeAfterHead() {
        return nodes.get(1);
    }

    private List<Node> getBodyNodes() {
        var bodyNodes = new LinkedList<>(nodes);
        bodyNodes.removeFirst();
        return bodyNodes;
    }

    Position getHeadPosition() {
        return getHead().getPosition();
    }

    Collection<Position> getBodyPositions() {
        return getBodyNodes()
                .stream()
                .map(Node::getPosition)
                .collect(toSet());
    }

    com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake toDto() {
        return new com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake(playerNumber, currentDirection, isAlive(), nodesDto());
    }

    private List<com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake.Node> nodesDto() {
        return nodes
                .stream()
                .map(Node::toDto)
                .collect(toList());
    }

    @AllArgsConstructor(access = PRIVATE)
    private static class Node {
        @Getter
        private Position position;
        private boolean hasFood;

        Option<Node> tryToGrow() {
            if (this.hasFood) {
                this.hasFood = false;
                return Option.of(new Node(position, false));
            }
            return Option.none();
        }

        boolean tryToEatFood(Position foodPosition) {
            if (foodPosition.equals(this.position)) {
                hasFood = true;
                return true;
            } else
                return false;
        }

        com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake.Node toDto() {
            return new com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake.Node(position, hasFood);
        }

        static Node notFed(Position position) {
            return new Node(position, false);
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    static class Creator {
        private static final int MIN_SNAKE_LENGTH = 2;
        private PlayerNumber playerNumber;
        private int snakeLength = MIN_SNAKE_LENGTH;
        private Position headPosition = Position.position(0, 0);
        private Direction headDirection = RIGHT;
        private Direction bodyDirectionFromHead = Direction.LEFT;
        private GridTeleport gridTeleport = new GridTeleport.TeleportDisabled();

        static Creator playerNumber(PlayerNumber playerNumber) {
            final Creator creator = new Creator();
            creator.playerNumber = playerNumber;
            return creator;
        }

        Creator length(int snakeLength) {
            this.snakeLength = Math.max(MIN_SNAKE_LENGTH, snakeLength);
            return this;
        }

        Creator head(Position position, Direction direction) {
            this.headPosition = position;
            this.headDirection = direction;
            return this;
        }

        Creator restOfBodyFromHead(Direction bodyDirection) {
            this.bodyDirectionFromHead = bodyDirection;
            return this;
        }

        Creator gridTeleport(GridTeleport gridTeleport) {
            this.gridTeleport = gridTeleport;
            return this;
        }

        Snake create() {
            if (bodyDirectionFromHead == headDirection)
                headDirection = getOpposite(headDirection);
            var nodes = createNodes(snakeLength, headPosition, bodyDirectionFromHead);
            return new Snake(playerNumber, nodes, gridTeleport, headDirection, new AtomicBoolean(false));
        }

        private LinkedList<Node> createNodes(int numberOfNodes, final Position startingPosition, Direction direction) {
            return IntStream
                    .range(0, numberOfNodes)
                    .mapToObj(offset -> startingPosition.move(direction, offset))
                    .map(Node::notFed)
                    .collect(toCollection(LinkedList::new));
        }

        private Direction getOpposite(Direction direction) {
            if (direction == UP)
                return DOWN;
            if (direction == DOWN)
                return UP;
            if (direction == RIGHT)
                return LEFT;
            else
                return RIGHT;
        }
    }
}