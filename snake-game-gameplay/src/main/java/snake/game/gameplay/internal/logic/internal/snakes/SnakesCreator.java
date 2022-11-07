package snake.game.gameplay.internal.logic.internal.snakes;

import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.game.dto.Walls;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.noscompany.snake.game.online.contract.messages.game.dto.Direction.*;
import static com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber.*;

public class SnakesCreator {
    private static final int SNAKE_LENGTH = 4;
    private static final int HEAD_OFFSET = SNAKE_LENGTH - 1;

    public static Snakes create(Set<PlayerNumber> playerNumbers, GridSize gridSize, Walls walls) {
        CrashedSnakesFinder crashedSnakesFinder = new CrashedSnakesFinder(gridSize);
        var wallTeleport = GridTeleport.create(gridSize, walls);
        var snakesMap = snakesMap(playerNumbers, gridSize, wallTeleport);
        return new Snakes(snakesMap, crashedSnakesFinder);
    }

    private static Map<PlayerNumber, Snake> snakesMap(Set<PlayerNumber> playerNumbers, GridSize gridSize, GridTeleport gridTeleport) {
        var snakesMap = new HashMap<PlayerNumber, Snake>();
        for (PlayerNumber playerNumber : playerNumbers) {
            var snake = createSnake(playerNumber, gridSize, gridTeleport);
            snakesMap.put(playerNumber, snake);
        }
        return snakesMap;
    }

    private static Snake createSnake(PlayerNumber playerNumber, GridSize gridSize, GridTeleport gridTeleport) {
        if (playerNumber == _1)
            return firstSnake(gridSize, gridTeleport);
        else if (playerNumber == _2)
            return secondSnake(gridSize, gridTeleport);
        else if (playerNumber == _3)
            return thirdSnake(gridSize, gridTeleport);
        else if (playerNumber == _4)
            return fourthSnake(gridSize, gridTeleport);
        throw new IllegalArgumentException("unknown player number: " + playerNumber);
    }

    static private Snake firstSnake(GridSize gridSize, GridTeleport gridTeleport) {
        var headPosition = gridSize
                .topLeftCorner()
                .moveRightBy(HEAD_OFFSET);
        return Snake.Creator
                .playerNumber(_1)
                .length(SNAKE_LENGTH)
                .head(headPosition, RIGHT)
                .restOfBodyFromHead(LEFT)
                .gridTeleport(gridTeleport)
                .create();
    }

    static private Snake secondSnake(GridSize gridSize, GridTeleport gridTeleport) {
        var headPosition = gridSize
                .bottomRightCorner()
                .moveLeftBy(HEAD_OFFSET);
        return Snake.Creator
                .playerNumber(_2)
                .length(SNAKE_LENGTH)
                .head(headPosition, LEFT)
                .restOfBodyFromHead(RIGHT)
                .gridTeleport(gridTeleport)
                .create();
    }

    static private Snake thirdSnake(GridSize gridSize, GridTeleport gridTeleport) {
        var headPosition = gridSize
                .bottomLeftCorner()
                .moveUpBy(HEAD_OFFSET);
        return Snake.Creator
                .playerNumber(_3)
                .length(SNAKE_LENGTH)
                .head(headPosition, UP)
                .restOfBodyFromHead(DOWN)
                .gridTeleport(gridTeleport)
                .create();
    }

    static private Snake fourthSnake(GridSize gridSize, GridTeleport gridTeleport) {
        var headPosition = gridSize
                .topRightCorner()
                .moveDownBy(HEAD_OFFSET);
        return Snake.Creator
                .playerNumber(_4)
                .length(SNAKE_LENGTH)
                .head(headPosition, DOWN)
                .restOfBodyFromHead(UP)
                .gridTeleport(gridTeleport)
                .create();
    }
}