package snake.game.gameplay;

import io.vavr.control.Either;
import snake.game.gameplay.dto.GameplayParams;
import snake.game.gameplay.internal.logic.GameLogicCreator;
import snake.game.gameplay.internal.runner.GameRunnerCreator;

class GameplayCreatorImpl implements GameplayCreator {

    @Override
    public Either<Error, Gameplay> createGame(GameplayParams params, GameplayEventHandler gameplayEventHandler) {
        if (params.getPlayerNumbers().isEmpty())
            return Either.left(Error.PLAYER_NUMBERS_ARE_NOT_SET);
        var gameLogic = GameLogicCreator.create(params.getPlayerNumbers(), params.getGridSize(), params.getWalls());
        var gameRunner = GameRunnerCreator.create(gameLogic, gameplayEventHandler, params.getGameSpeed(), params.getCountdownTime());
        return Either.right(new GameplayImpl(gameLogic, gameRunner));
    }
}