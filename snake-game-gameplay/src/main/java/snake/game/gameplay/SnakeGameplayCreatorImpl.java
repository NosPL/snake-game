package snake.game.gameplay;

import io.vavr.control.Either;
import snake.game.gameplay.dto.GameplayParams;
import snake.game.gameplay.internal.logic.GameLogicCreator;
import snake.game.gameplay.internal.runner.GameRunnerCreator;

class SnakeGameplayCreatorImpl implements SnakeGameplayCreator {

    @Override
    public Either<Error, SnakeGameplay> createGame(GameplayParams params, SnakeGameplayEventHandler snakeGameplayEventHandler) {
        if (params.getPlayerNumbers().isEmpty())
            return Either.left(Error.PLAYER_NUMBERS_ARE_NOT_SET);
        var gameLogic = GameLogicCreator.create(params.getPlayerNumbers(), params.getGridSize(), params.getWalls());
        var gameRunner = GameRunnerCreator.create(gameLogic, snakeGameplayEventHandler, params.getGameSpeed(), params.getCountdownTime());
        return Either.right(new SnakeGameplayImpl(gameLogic, gameRunner));
    }
}