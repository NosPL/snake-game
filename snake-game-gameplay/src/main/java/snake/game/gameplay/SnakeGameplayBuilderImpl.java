package snake.game.gameplay;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import com.noscompany.snake.game.online.contract.messages.game.events.*;
import io.vavr.control.Either;
import lombok.NonNull;
import snake.game.gameplay.internal.logic.GameLogicCreator;
import snake.game.gameplay.internal.runner.GameRunnerCreator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class SnakeGameplayBuilderImpl implements SnakeGameplayBuilder {
    private Set<PlayerNumber> players = Set.of();
    private GameSpeed gameSpeed = GameSpeed.x1;
    private GridSize gridSize = GridSize._10x10;
    private Walls walls = Walls.ON;
    private CountdownTime countdownTime = CountdownTime.inSeconds(0);
    private SnakeGameEventHandler eventHandler = new IdleEventHandler();

    @Override
    public SnakeGameplayBuilder set(@NonNull PlayerNumber player) {
        this.players = Set.of(player);
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull Collection<PlayerNumber> players) {
        this.players = new HashSet<>(players);
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull GameSpeed gameSpeed) {
        this.gameSpeed = gameSpeed;
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull GridSize gridSize) {
        this.gridSize = gridSize;
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull Walls walls) {
        this.walls = walls;
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull CountdownTime countdownTime) {
        this.countdownTime = countdownTime;
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull SnakeGameEventHandler eventHandler) {
        this.eventHandler = eventHandler;
        return this;
    }

    @Override
    public Either<Error, SnakeGameplay> createGame() {
        if (players.isEmpty())
            return Either.left(Error.PLAYERS_ARE_NOT_SET);
        var gameLogic = GameLogicCreator.create(players, gridSize, walls);
        var gameRunner = GameRunnerCreator.create(gameLogic, eventHandler, gameSpeed, countdownTime);
        return Either.right(new SnakeGameplayImpl(gameLogic, gameRunner));
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