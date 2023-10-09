package com.noscompany.snake.game.online.playground.test.commons;

import com.noscompany.message.publisher.utils.NullMessagePublisher;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.playground.PlaygroundState;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import com.noscompany.snake.game.online.playground.Playground;
import com.noscompany.snake.game.online.playground.PlaygroundConfiguration;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.Before;

import java.util.stream.Stream;

public class PlaygroundTestSetup {
    protected int userIdCounter;
    protected int userNameCounter;
    protected Playground playground;
    protected UserId actorId;
    protected UserName actorName;

    @Before
    public void init() {
        userIdCounter = 0;
        userNameCounter = 0;
        playground = new PlaygroundConfiguration().createPlayground(new NullMessagePublisher(), new GameRunningEndlesslyAfterStartCreator());
        actorId = UserId.random();
        actorName = UserName.random();
    }

    protected PlayerNumber anyPlayerNumber() {
        return Stream.of(PlayerNumber.values()).findAny().get();
    }

    protected boolean isSuccess(Option<FailedToStartGame> startGameResult) {
        return startGameResult.isEmpty();
    }

    protected <L, R> Either<L, R> failure(L failure) {
        return Either.left(failure);
    }

    protected <L, R> Either<L, R> success(R success) {
        return Either.right(success);
    }

    protected PlaygroundState playgroundState() {
        return playground.getPlaygroundState();
    }

    protected GameOptions currentGameOptions() {
        return playgroundState().getGameOptions();
    }

    protected GameOptions newGameOptions() {
        return new GameOptions(newGridSize(), newGameSpeed(), newWalls());
    }

    private GridSize newGridSize() {
        return Stream.of(GridSize.values())
                .filter(gridSize -> !gridSize.equals(currentGameOptions().getGridSize()))
                .findAny()
                .get();
    }

    private GameSpeed newGameSpeed() {
        return Stream.of(GameSpeed.values())
                .filter(gameSpeed -> !gameSpeed.equals(currentGameOptions().getGameSpeed()))
                .findAny()
                .get();
    }

    private Walls newWalls() {
        return Stream.of(Walls.values())
                .filter(walls -> !walls.equals(currentGameOptions().getWalls()))
                .findAny()
                .get();
    }
}