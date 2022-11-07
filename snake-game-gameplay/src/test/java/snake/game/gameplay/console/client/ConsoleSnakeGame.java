package snake.game.gameplay.console.client;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import snake.game.gameplay.SnakeGame;
import snake.game.gameplay.SnakeGameCreator;
import snake.game.gameplay.console.client.output.ConsolePrinter;
import com.noscompany.snake.game.online.contract.messages.game.dto.CountdownTime;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;

import static com.noscompany.snake.game.online.contract.messages.game.dto.Direction.*;
import static lombok.AccessLevel.PRIVATE;

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
                .set(new ConsolePrinter())
                .createGame()
                .map(game -> new ConsoleSnakeGame(game, playerNumber, consoleInput));
    }
}