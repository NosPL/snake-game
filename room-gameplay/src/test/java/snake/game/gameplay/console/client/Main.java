package snake.game.gameplay.console.client;


import snake.game.gameplay.SnakeGameplayCreator;

import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        var consoleInput = new ConsoleInput(new Scanner(System.in));
        var gameSettingsService = new GameplayParamsService(consoleInput);
        System.out.println("Snake Game");
        var gameSettings = gameSettingsService.getParams();
        System.out.println("Press ENTER to start");
        consoleInput.waitForEnterPress();
        ConsoleSnakeGame
                .createFrom(gameSettings, consoleInput)
                .peek(ConsoleSnakeGame::start)
                .peekLeft(Main::handleError);
    }

    private static void handleError(SnakeGameplayCreator.Error error) {
        System.out.println(error.toString());
    }
}