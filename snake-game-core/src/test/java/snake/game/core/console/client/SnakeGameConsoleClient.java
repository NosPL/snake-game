package snake.game.core.console.client;

import lombok.RequiredArgsConstructor;
import snake.game.core.SnakeGameConfiguration;

import java.util.Scanner;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
class SnakeGameConsoleClient {
    private final ConsoleInput consoleInput;
    private final GameSettingsService gameSettingsService;

    public static void main(String[] args) {
        SnakeGameConsoleClient
                .getInstance()
                .start();
    }

    static SnakeGameConsoleClient getInstance() {
        var consoleInput = new ConsoleInput(new Scanner(System.in));
        var gameSettingsService = new GameSettingsService(consoleInput);
        return new SnakeGameConsoleClient(consoleInput, gameSettingsService);
    }

    void start() {
        System.out.println("Snake Game");
        var gameSettings = gameSettingsService.getSettings();
        System.out.println("Press ENTER to start");
        consoleInput.waitForEnterPress();
        ConsoleSnakeGame.createFrom(gameSettings, consoleInput)
                .peek(ConsoleSnakeGame::start)
                .peekLeft(this::handleError);
    }

    private void handleError(SnakeGameConfiguration.Error error) {
        System.out.println(error.toString());
    }
}