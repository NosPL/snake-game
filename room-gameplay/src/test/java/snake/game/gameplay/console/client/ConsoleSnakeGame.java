package snake.game.gameplay.console.client;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import snake.game.gameplay.Gameplay;
import snake.game.gameplay.GameplayConfiguration;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.console.client.output.ConsolePrinter;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import snake.game.gameplay.dto.GameplayParams;

import static com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction.*;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
class ConsoleSnakeGame {
    private final Gameplay gameplay;
    private final PlayerNumber playerNumber;
    private final ConsoleInput consoleInput;

    void start() {
        gameplay.start();
        while (gameplay.isRunning()) {
            var s = consoleInput.getString();
            if ("w".equalsIgnoreCase(s))
                gameplay.changeSnakeDirection(playerNumber, UP);
            else if ("s".equalsIgnoreCase(s))
                gameplay.changeSnakeDirection(playerNumber, DOWN);
            else if ("a".equalsIgnoreCase(s))
                gameplay.changeSnakeDirection(playerNumber, LEFT);
            else if ("d".equalsIgnoreCase(s))
                gameplay.changeSnakeDirection(playerNumber, RIGHT);
            else if ("q".equalsIgnoreCase(s))
                gameplay.cancel();
            else if ("p".equalsIgnoreCase(s))
                gameplay.pause();
            else if ("r".equalsIgnoreCase(s))
                gameplay.resume();
        }
    }

    static Either<GameplayCreator.Error, ConsoleSnakeGame> createFrom(GameplayParams params,
                                                                      ConsoleInput consoleInput) {
        return new GameplayConfiguration()
                .snakeGameplayCreator()
                .createGame(params, new ConsolePrinter())
                .map(game -> new ConsoleSnakeGame(game, getPlayerNumber(params), consoleInput));
    }

    private static PlayerNumber getPlayerNumber(GameplayParams params) {
        return params.getPlayerNumbers().stream().findFirst().get();
    }
}