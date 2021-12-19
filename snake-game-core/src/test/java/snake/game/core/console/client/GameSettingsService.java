package snake.game.core.console.client;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.dto.Walls;

@RequiredArgsConstructor
class GameSettingsService {
    private final ConsoleInput consoleInput;

    GameSettings getSettings() {
        return new GameSettings(getSnakeNumber(), getSpeed(), getGridSize(), getWalls());
    }

    private SnakeNumber getSnakeNumber() {
        System.out.println("set player number:");
        System.out.println("1 - top left corner");
        System.out.println("2 - bottom right corner");
        System.out.println("3 - bottom left corner");
        System.out.println("4 - top right corner");
        SnakeNumber snakeNumber;
        do {
            snakeNumber = consoleInput
                    .getInt()
                    .map(this::getSnakeNumber)
                    .getOrNull();
            if (snakeNumber == null)
                System.out.println("wrong input");
        } while (snakeNumber == null);
        return snakeNumber;
    }

    private GameSpeed getSpeed() {
        System.out.println("set speed (from 1 to 8):");
        GameSpeed gameSpeed;
        do {
            gameSpeed = consoleInput
                    .getInt()
                    .map(this::getSpeed)
                    .getOrNull();
            if (gameSpeed == null)
                System.out.println("wrong input");
        } while (gameSpeed == null);
        return gameSpeed;
    }

    private GridSize getGridSize() {
        System.out.println("set grid size:");
        System.out.println("1 - 10 x 10");
        System.out.println("2 - 15 x 15");
        System.out.println("3 - 20 x 20");
        System.out.println("4 - 25 x 25");
        GridSize gridSize;
        do {
            gridSize = consoleInput
                    .getInt()
                    .map(this::getGridSize)
                    .getOrNull();
            if (gridSize == null)
                System.out.println("wrong input");
        } while (gridSize == null);
        return gridSize;
    }

    private Walls getWalls() {
        System.out.println("set walls:");
        System.out.println("1 - ON");
        System.out.println("2 - OFF");
        Walls walls;
        do {
            walls = consoleInput
                    .getInt()
                    .map(this::getWalls)
                    .getOrNull();
            if (walls == null)
                System.out.println("wrong input");
        } while (walls == null);
        return walls;
    }

    private SnakeNumber getSnakeNumber(Integer integer) {
        return Try
                .of(() -> SnakeNumber.values()[integer - 1])
                .getOrNull();
    }

    private GameSpeed getSpeed(Integer integer) {
        return Try
                .of(() -> GameSpeed.values()[integer - 1])
                .getOrNull();
    }

    private GridSize getGridSize(Integer integer) {
        return Try
                .of(() -> GridSize.values()[integer - 1])
                .getOrNull();
    }

    private Walls getWalls(Integer integer) {
        return Try
                .of(() -> Walls.values()[integer - 1])
                .getOrNull();
    }
}