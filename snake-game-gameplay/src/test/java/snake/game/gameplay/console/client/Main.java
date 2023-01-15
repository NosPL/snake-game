package snake.game.gameplay.console.client;


import snake.game.gameplay.SnakeGameplayBuilder;

import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        var consoleInput = new ConsoleInput(new Scanner(System.in));
        var gameSettingsService = new GameSettingsService(consoleInput);
        System.out.println("Snake Game");
        var gameSettings = gameSettingsService.getSettings();
        System.out.println("Press ENTER to start");
        consoleInput.waitForEnterPress();
        ConsoleSnakeGame
                .createFrom(gameSettings, consoleInput)
                .peek(ConsoleSnakeGame::start)
                .peekLeft(Main::handleError);
    }

    private static void handleError(SnakeGameplayBuilder.Error error) {
        System.out.println(error.toString());
    }
}