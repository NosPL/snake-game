package snake.game.gameplay;

import io.vavr.control.Either;
import snake.game.gameplay.dto.GameplayParams;

public interface GameplayCreator {

    Either<Error, Gameplay> createGame(GameplayParams gameplayParams, GameplayEventHandler gameplayEventHandler);

    enum Error {
        PLAYER_NUMBERS_ARE_NOT_SET;
    }
}