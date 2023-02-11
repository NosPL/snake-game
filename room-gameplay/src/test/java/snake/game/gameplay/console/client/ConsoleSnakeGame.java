package snake.game.gameplay.console.client;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import snake.game.gameplay.SnakeGameplay;
import snake.game.gameplay.SnakeGameplayConfiguration;
import snake.game.gameplay.SnakeGameplayCreator;
import snake.game.gameplay.console.client.output.ConsolePrinter;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import snake.game.gameplay.dto.GameplayParams;

import static com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction.*;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
class ConsoleSnakeGame {
    private final SnakeGameplay snakeGameplay;
    private final PlayerNumber playerNumber;
    private final ConsoleInput consoleInput;

    void start() {
        snakeGameplay.start();
        while (snakeGameplay.isRunning()) {
            var s = consoleInput.getString();
            if ("w".equalsIgnoreCase(s))
                snakeGameplay.changeSnakeDirection(playerNumber, UP);
            else if ("s".equalsIgnoreCase(s))
                snakeGameplay.changeSnakeDirection(playerNumber, DOWN);
            else if ("a".equalsIgnoreCase(s))
                snakeGameplay.changeSnakeDirection(playerNumber, LEFT);
            else if ("d".equalsIgnoreCase(s))
                snakeGameplay.changeSnakeDirection(playerNumber, RIGHT);
            else if ("q".equalsIgnoreCase(s))
                snakeGameplay.cancel();
            else if ("p".equalsIgnoreCase(s))
                snakeGameplay.pause();
            else if ("r".equalsIgnoreCase(s))
                snakeGameplay.resume();
        }
    }

    static Either<SnakeGameplayCreator.Error, ConsoleSnakeGame> createFrom(GameplayParams params,
                                                                           ConsoleInput consoleInput) {
        return new SnakeGameplayConfiguration()
                .snakeGameplayCreator()
                .createGame(params, new ConsolePrinter())
                .map(game -> new ConsoleSnakeGame(game, getPlayerNumber(params), consoleInput));
    }

    private static PlayerNumber getPlayerNumber(GameplayParams params) {
        return params.getPlayerNumbers().stream().findFirst().get();
    }
}