package snake.game.core.console.client;

import lombok.RequiredArgsConstructor;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.SnakeGame;
import snake.game.core.console.client.input.ConsoleInput;
import snake.game.core.console.client.output.ConsoleEventHandler;
import snake.game.core.dto.CountdownTime;

import java.util.Scanner;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
class SnakeGameConsoleClient {
    private final ConsoleInput consoleInput;
    private final GameSettingsService gameSettingsService;

    public static void main(String[] args) {
        SnakeGameConsoleClient
                .getInstance()
                .startGame();
    }

    static SnakeGameConsoleClient getInstance() {
        var consoleInput = new ConsoleInput(new Scanner(System.in));
        var gameSettingsService = new GameSettingsService(consoleInput);
        return new SnakeGameConsoleClient(consoleInput, gameSettingsService);
    }

    void startGame() {
        System.out.println("Snake Game");
        var gameSettings = gameSettingsService.getSettings();
        System.out.println("Press ENTER to start");
        consoleInput.waitForEnterPress();
        var snakeGame = createGame(gameSettings);
        snakeGame.start();
        while (snakeGame.isRunning()) {
            consoleInput
                    .getUserCommand()
                    .onDirectionChange(snakeGame::changeSnakesDirection)
                    .onExit(snakeGame::cancel)
                    .onPause(snakeGame::pause)
                    .onResume(snakeGame::resume)
                    .onUnknownCommand(this::printUnknownCommandError);
        }
    }

    private void printUnknownCommandError(String unknownCommand) {
        if (!unknownCommand.isBlank())
            System.out.println("unknown command: " + unknownCommand);
    }

    private SnakeGame createGame(GameSettings gameSettings) {
        return new SnakeGameConfiguration()
                .set(gameSettings.getSnakeNumber())
                .set(CountdownTime.inSeconds(3))
                .set(gameSettings.getGridSize())
                .set(gameSettings.getGameSpeed())
                .set(gameSettings.getWalls())
                .set(new ConsoleEventHandler())
                .create()
                .get();
    }
}