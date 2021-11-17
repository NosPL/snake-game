package snake.game.core;

import io.vavr.control.Either;
import lombok.NonNull;
import snake.game.core.dto.*;
import snake.game.core.events.*;
import snake.game.core.logic.GameLogicCreator;
import snake.game.core.runner.GameRunnerCreator;

import java.util.Set;

public final class SnakeGameConfiguration {
    private Set<SnakeNumber> snakeNumbers = Set.of();
    private GameSpeed gameSpeed = GameSpeed.x1;
    private GridSize gridSize = GridSize._10x10;
    private Walls walls = Walls.ON;
    private CountdownTime countdownTime = CountdownTime.inSeconds(0);
    private SnakeGameEventHandler eventHandler = new IdleEventHandler();

    public SnakeGameConfiguration set(@NonNull SnakeNumber snakeNumber) {
        this.snakeNumbers = Set.of(snakeNumber);
        return this;
    }

    public SnakeGameConfiguration set(@NonNull Set<SnakeNumber> snakeNumbers) {
        this.snakeNumbers = snakeNumbers;
        return this;
    }

    public SnakeGameConfiguration set(@NonNull GameSpeed gameSpeed) {
        this.gameSpeed = gameSpeed;
        return this;
    }

    public SnakeGameConfiguration set(@NonNull GridSize gridSize) {
        this.gridSize = gridSize;
        return this;
    }

    public SnakeGameConfiguration set(@NonNull Walls walls) {
        this.walls = walls;
        return this;
    }

    public SnakeGameConfiguration set(@NonNull CountdownTime countdownTime) {
        this.countdownTime = countdownTime;
        return this;
    }

    public SnakeGameConfiguration set(@NonNull SnakeGameEventHandler eventHandler) {
        this.eventHandler = eventHandler;
        return this;
    }

    public Either<Error, SnakeGame> create() {
        if (snakeNumbers.isEmpty())
            return Either.left(Error.PLAYERS_SET_IS_EMPTY);
        var gameLogic = GameLogicCreator.create(snakeNumbers, walls, gridSize);
        var gameRunner = GameRunnerCreator.create(gameLogic, eventHandler, gameSpeed, countdownTime);
        return Either.right(new SnakeGameImpl(gameLogic, gameRunner));
    }

    public enum Error {
        PLAYERS_SET_IS_EMPTY;
    }

    private class IdleEventHandler implements SnakeGameEventHandler {

        @Override
        public void handle(TimeLeftToGameStartHasChanged event) {

        }

        @Override
        public void handle(GameStarted event) {

        }

        @Override
        public void handle(GameContinues event) {

        }

        @Override
        public void handle(GameFinished event) {

        }

        @Override
        public void handle(GameCancelled event) {

        }

        @Override
        public void handle(GamePaused event) {

        }

        @Override
        public void handle(GameResumed event) {

        }
    }
}