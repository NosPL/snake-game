package snake.game.core.logic.snakes;

import snake.game.core.dto.GridSize;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.dto.Walls;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static snake.game.core.dto.Direction.*;
import static snake.game.core.dto.SnakeNumber.*;
import static snake.game.core.logic.snakes.Snake.Creator.Head.at;

public class SnakesCreator {

    public static Snakes createSnakes(Set<SnakeNumber> snakeNumbers, GridSize gridSize, Walls walls) {
        var wallTeleport = GridTeleport.create(gridSize, walls);
        var snakesMap = snakesMap(snakeNumbers, gridSize, wallTeleport);
        return new Snakes(snakesMap);
    }

    private static Map<SnakeNumber, Snake> snakesMap(Set<SnakeNumber> snakeNumbers, GridSize gridSize, GridTeleport wallTeleport) {
        var snakesMap = new HashMap<SnakeNumber, Snake>();
        for (SnakeNumber snakeNumber : snakeNumbers) {
            var snake = createSnake(snakeNumber, gridSize, wallTeleport);
            snakesMap.put(snakeNumber, snake);
        }
        return snakesMap;
    }

    private static Snake createSnake(SnakeNumber snakeNumber, GridSize gridSize, GridTeleport gridTeleport) {
        if (snakeNumber == _1)
            return first(gridSize, gridTeleport);
        else if (snakeNumber == _2)
            return second(gridSize, gridTeleport);
        else if (snakeNumber == _3)
            return third(gridSize, gridTeleport);
        else if (snakeNumber == _4)
            return fourth(gridSize, gridTeleport);
        throw new IllegalArgumentException("unknown player number: " + snakeNumber);
    }

    static private Snake first(GridSize gridSize, GridTeleport gridTeleport) {
        var point = gridSize
                .topLeftCorner()
                .moveRightBy(3);
        return Snake.Creator
                .playerNumber(_1)
                .head(at(point).inDirection(RIGHT))
                .restOfBodyFromHead(LEFT)
                .wallTeleport(gridTeleport)
                .create();
    }

    static private Snake second(GridSize gridSize, GridTeleport gridTeleport) {
        var point = gridSize
                .bottomRightCorner()
                .moveLeftBy(3);
        return Snake.Creator
                .playerNumber(_2)
                .head(at(point).inDirection(LEFT))
                .restOfBodyFromHead(RIGHT)
                .wallTeleport(gridTeleport)
                .create();
    }

    static private Snake third(GridSize gridSize, GridTeleport gridTeleport) {
        var point = gridSize
                .bottomLeftCorner()
                .moveUpBy(3);
        return Snake.Creator
                .playerNumber(_3)
                .head(at(point).inDirection(UP))
                .restOfBodyFromHead(DOWN)
                .wallTeleport(gridTeleport)
                .create();
    }

    static private Snake fourth(GridSize gridSize, GridTeleport gridTeleport) {
        var point = gridSize
                .topRightCorner()
                .moveDownBy(3);
        return Snake.Creator
                .playerNumber(_4)
                .head(at(point).inDirection(DOWN))
                .restOfBodyFromHead(UP)
                .wallTeleport(gridTeleport)
                .create();
    }
}
