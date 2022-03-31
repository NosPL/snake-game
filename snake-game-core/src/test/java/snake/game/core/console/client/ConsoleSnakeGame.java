package snake.game.core.console.client;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import snake.game.core.SnakeGame;
import snake.game.core.SnakeGameCreator;
import snake.game.core.console.client.output.ConsoleEventHandler;
import snake.game.core.dto.CountdownTime;
import snake.game.core.dto.PlayerNumber;

import static lombok.AccessLevel.PRIVATE;
import static snake.game.core.dto.Direction.*;

@AllArgsConstructor(access = PRIVATE)
class ConsoleSnakeGame {
    private final SnakeGame snakeGame;
    private final PlayerNumber playerNumber;
    private final ConsoleInput consoleInput;

    void start() {
        snakeGame.start();
        while (snakeGame.isRunning()) {
            var s = consoleInput.getString();
            if ("w".equalsIgnoreCase(s))
                snakeGame.changeSnakeDirection(playerNumber, UP);
            else if ("s".equalsIgnoreCase(s))
                snakeGame.changeSnakeDirection(playerNumber, DOWN);
            else if ("a".equalsIgnoreCase(s))
                snakeGame.changeSnakeDirection(playerNumber, LEFT);
            else if ("d".equalsIgnoreCase(s))
                snakeGame.changeSnakeDirection(playerNumber, RIGHT);
            else if ("q".equalsIgnoreCase(s))
                snakeGame.cancel();
            else if ("p".equalsIgnoreCase(s))
                snakeGame.pause();
            else if ("r".equalsIgnoreCase(s))
                snakeGame.resume();
        }
    }

    static Either<SnakeGameCreator.Error, ConsoleSnakeGame> createFrom(GameSettings settings,
                                                                       ConsoleInput consoleInput) {
        PlayerNumber playerNumber = settings.getPlayerNumber();
        return new SnakeGameCreator()
                .set(playerNumber)
                .set(CountdownTime.inSeconds(3))
                .set(settings.getGridSize())
                .set(settings.getGameSpeed())
                .set(settings.getWalls())
                .set(new ConsoleEventHandler())
                .createGame()
                .map(game -> new ConsoleSnakeGame(game, playerNumber, consoleInput));
    }
}