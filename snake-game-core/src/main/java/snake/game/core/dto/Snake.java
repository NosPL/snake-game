package snake.game.core.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.LinkedList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class Snake {
    PlayerNumber playerNumber;
    Direction direction;
    boolean alive;
    List<Node> nodes;

    public Node getHeadNode() {
        return nodes.get(0);
    }

    public List<Node> getBodyNodes() {
        LinkedList<Node> bodyNodes = new LinkedList<>(nodes);
        bodyNodes.removeFirst();
        return bodyNodes;
    }

    @Value
    @NoArgsConstructor(force = true, access = PRIVATE)
    @AllArgsConstructor
    public static class Node {
        Position position;
        boolean fed;
    }
}