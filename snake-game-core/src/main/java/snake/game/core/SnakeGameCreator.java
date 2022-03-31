package snake.game.core;

import io.vavr.control.Either;
import lombok.NonNull;
import snake.game.core.dto.*;
import snake.game.core.dto.events.*;
import snake.game.core.internal.logic.GameLogicCreator;
import snake.game.core.internal.runner.GameRunnerCreator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SnakeGameCreator {
    private Set<PlayerNumber> players = Set.of();
    private GameSpeed gameSpeed = GameSpeed.x1;
    private GridSize gridSize = GridSize._10x10;
    private Walls walls = Walls.ON;
    private CountdownTime countdownTime = CountdownTime.inSeconds(0);
    private SnakeGameEventHandler eventHandler = new IdleEventHandler();

    public SnakeGameCreator set(@NonNull PlayerNumber player) {
        this.players = Set.of(player);
        return this;
    }

    public SnakeGameCreator set(@NonNull Collection<PlayerNumber> players) {
        this.players = new HashSet<>(players);
        return this;
    }

    public SnakeGameCreator set(@NonNull GameSpeed gameSpeed) {
        this.gameSpeed = gameSpeed;
        return this;
    }

    public SnakeGameCreator set(@NonNull GridSize gridSize) {
        this.gridSize = gridSize;
        return this;
    }

    public SnakeGameCreator set(@NonNull Walls walls) {
        this.walls = walls;
        return this;
    }

    public SnakeGameCreator set(@NonNull CountdownTime countdownTime) {
        this.countdownTime = countdownTime;
        return this;
    }

    public SnakeGameCreator set(@NonNull SnakeGameEventHandler eventHandler) {
        this.eventHandler = eventHandler;
        return this;
    }

    public Either<Error, SnakeGame> createGame() {
        if (players.isEmpty())
            return Either.left(Error.PLAYERS_ARE_NOT_SET);
        var gameLogic = GameLogicCreator.create(players, gridSize, walls);
        var gameRunner = GameRunnerCreator.create(gameLogic, eventHandler, gameSpeed, countdownTime);
        return Either.right(new SnakeGameImpl(gameLogic, gameRunner));
    }

    public enum Error {
        PLAYERS_ARE_NOT_SET;
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