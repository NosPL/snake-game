package snake.game.gameplay;

import io.vavr.control.Either;
import snake.game.gameplay.dto.GameplayParams;

public interface SnakeGameplayCreator {

    Either<Error, SnakeGameplay> createGame(GameplayParams gameplayParams, SnakeGameplayEventHandler snakeGameplayEventHandler);

    enum Error {
        PLAYER_NUMBERS_ARE_NOT_SET;
    }
}