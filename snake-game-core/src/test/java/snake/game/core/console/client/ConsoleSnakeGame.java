package snake.game.core.console.client;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import snake.game.core.SnakeGame;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.console.client.output.ConsoleEventHandler;
import snake.game.core.dto.CountdownTime;
import snake.game.core.dto.SnakeNumber;

import static lombok.AccessLevel.PRIVATE;
import static snake.game.core.dto.Direction.*;

@AllArgsConstructor(access = PRIVATE)
class ConsoleSnakeGame {
    private final SnakeGame snakeGame;
    private final SnakeNumber snakeNumber;
    private final ConsoleInput consoleInput;

    void start() {
        snakeGame.start();
        while (snakeGame.isRunning()) {
            var s = consoleInput.getString();
            if ("w".equalsIgnoreCase(s))
                snakeGame.changeSnakeDirection(snakeNumber, UP);
            if ("s".equalsIgnoreCase(s))
                snakeGame.changeSnakeDirection(snakeNumber, DOWN);
            if ("a".equalsIgnoreCase(s))
                snakeGame.changeSnakeDirection(snakeNumber, LEFT);
            if ("d".equalsIgnoreCase(s))
                snakeGame.changeSnakeDirection(snakeNumber, RIGHT);
            if ("q".equalsIgnoreCase(s))
                snakeGame.cancel();
            if ("p".equalsIgnoreCase(s))
                snakeGame.pause();
            if ("r".equalsIgnoreCase(s))
                snakeGame.resume();
        }
    }

    static Either<SnakeGameConfiguration.Error, ConsoleSnakeGame> createFrom(GameSettings settings,
                                                                             ConsoleInput consoleInput) {
        SnakeNumber snakeNumber = settings.getSnakeNumber();
        return new SnakeGameConfiguration()
                .set(snakeNumber)
                .set(CountdownTime.inSeconds(3))
                .set(settings.getGridSize())
                .set(settings.getGameSpeed())
                .set(settings.getWalls())
                .set(new ConsoleEventHandler())
                .create()
                .map(game -> new ConsoleSnakeGame(game, snakeNumber, consoleInput));
    }
}