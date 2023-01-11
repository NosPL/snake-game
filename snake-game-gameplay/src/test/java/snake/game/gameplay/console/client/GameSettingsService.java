package snake.game.gameplay.console.client;

import lombok.RequiredArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.game.dto.Walls;

@RequiredArgsConstructor
class GameSettingsService {
    private final ConsoleInput consoleInput;

    GameSettings getSettings() {
        PlayerNumber snakeNumber = getPlayerNumber();
        GameSpeed gameSpeed = getGameSpeed();
        GridSize gridSize = getGridSize();
        Walls walls = getWalls();
        return new GameSettings(snakeNumber, gameSpeed, gridSize, walls);
    }

    private PlayerNumber getPlayerNumber() {
        System.out.println("set player number:");
        System.out.println("1 - top left corner");
        System.out.println("2 - bottom right corner");
        System.out.println("3 - bottom left corner");
        System.out.println("4 - top right corner");
        PlayerNumber playerNumber;
        do {
            playerNumber = consoleInput
                    .getInt()
                    .map(i -> PlayerNumber.values()[i-1])
                    .getOrNull();
            if (playerNumber == null)
                System.out.println("wrong input");
        } while (playerNumber == null);
        return playerNumber;
    }

    private GameSpeed getGameSpeed() {
        System.out.println("set speed (from 1 to 8):");
        GameSpeed gameSpeed;
        do {
            gameSpeed = consoleInput
                    .getInt()
                    .map(i -> GameSpeed.values()[i-1])
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
                    .map(i -> GridSize.values()[i-1])
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
                    .map(i -> Walls.values()[i-1])
                    .getOrNull();
            if (walls == null)
                System.out.println("wrong input");
        } while (walls == null);
        return walls;
    }
}